package com.tpson.kuluagent.netty.server;

import com.tpson.kuluagent.domain.Backend;
import com.tpson.kuluagent.netty.client.KuluAgent;
import com.tpson.kuluagent.netty.loadbalance.HashLoadBalance;
import com.tpson.kuluagent.netty.loadbalance.LoadBalances;
import com.tpson.kuluagent.netty.server.watch.WatchServerConfig;
import com.tpson.kuluagent.service.ProtocalService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.bind.DatatypeConverter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Zhangka in 2018/04/20
 */
public abstract class KuluAgentServerHandler extends ChannelInboundHandlerAdapter {
    protected static final int MAX_BUFFER_SIZE = 256;
    protected final AttributeKey<Channel> k = AttributeKey.valueOf("right");
    protected final AttributeKey<ByteBuf> b = AttributeKey.valueOf("buffers");
    private final AtomicInteger connections = new AtomicInteger(0);
    private static final Logger LOGGER = LoggerFactory.getLogger(KuluAgentServerHandler.class);

    @Autowired
    WatchServerConfig config;
    @Autowired
    LoadBalances loadBalances;
    @Autowired
    ProtocalService protocalService;
    @Autowired
    KuluAgent kuluAgent;
    @Autowired
    FlowControlExecutor flowControlExecutor;
    @Autowired
    HashLoadBalance hashLoadBalance;

    public abstract String getOwnerName();
    public abstract List<LoadBalances.Key> decode(List<LoadBalances.Key> keys);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        final Channel proxy = ctx.channel().attr(k).get();

        LOGGER.info(getOwnerName() + "-Connections:" + connections.get());
        // 已连接，直接转发
        if (proxy != null) {
            boolean autoRead = proxy.isWritable() ? true : false;
            proxy.config().setAutoRead(autoRead);
            proxy.writeAndFlush(msg);                   //转发
            return;
        }

        // ===================================================
        // 处理分包
        ByteBuf buf = ctx.channel().attr(b).get();
        if (buf != null) {
            buf.writeBytes((ByteBuf)msg);
        } else {
            buf = (ByteBuf)msg;
        }

        int count = buf.readableBytes();
        byte[] dst = new byte[count];
        buf.getBytes(0, dst);
        LOGGER.info("remote:" + ctx.channel().remoteAddress().toString() + "|content hex:" + DatatypeConverter.printHexBinary(dst));

        Backend backend;
        if (hashLoadBalance.getBackends(getOwnerName()).size() > 0) {// 没有配置HASH服务器，不解析key.
            List<LoadBalances.Key> keys = protocalService.getKeys(getOwnerName(), buf);
            if (keys == null) {
                if (buf.readableBytes() >= MAX_BUFFER_SIZE) {
                    close(ctx);
                } else {
                    ByteBuf buffer = ctx.channel().attr(b).get();
                    if (buffer == null) {
                        buffer = Unpooled.buffer(64);
                        buf.readerIndex(0);
                        buffer.writeBytes(buf);
                        ctx.channel().attr(b).setIfAbsent(buffer);
                    }
                }
                return;
            }
            keys = decode(keys);
            LOGGER.info("keys:" + keys);
            LoadBalances.Key[] ks = new LoadBalances.Key[keys.size()];
            backend = loadBalances.loadBalance(getOwnerName(), keys.toArray(ks));
        } else {
            backend = loadBalances.loadBalance(getOwnerName());
        }
        LOGGER.info(backend == null ? "KuluAgent-" + getOwnerName() + "找不到路由服务器." : backend.toString());
        if (backend == null) {
            close(ctx);
            return;
        }

        Channel left = ctx.channel();
        Channel right = kuluAgent.connect(backend);
        left.attr(k).setIfAbsent(right);
        right.attr(k).setIfAbsent(left);
        flowControlExecutor.addChannel(left);
        flowControlExecutor.addChannel(right);
        buf.readerIndex(0);
        right.writeAndFlush(buf);    // 转发
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error("exceptionCaught", cause);
        close(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        LOGGER.info("channelInactive");
        connections.decrementAndGet();
        close(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        LOGGER.info("channelActive");
        connections.incrementAndGet();
    }

    public void close(ChannelHandlerContext ctx) {
        final Channel left = ctx.channel();
        final Channel right = left.attr(k).get();

        if (left != null && left.isActive()) {
            LOGGER.info("left.close():" + left);
            left.close();
        }
        if (right != null && right.isActive()) {
            LOGGER.info("right.close():" + right);
            right.close();
        }

        LOGGER.info("Connections:" + connections.get());
    }
}
