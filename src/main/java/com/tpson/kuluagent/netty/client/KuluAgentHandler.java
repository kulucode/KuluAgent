package com.tpson.kuluagent.netty.client;

import com.tpson.kuluagent.netty.loadbalance.LoadBalances;
import com.tpson.kuluagent.netty.server.KuluAgentServerHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.util.List;

@Component
@ChannelHandler.Sharable
public class KuluAgentHandler extends KuluAgentServerHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(KuluAgentHandler.class);
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf buf = (ByteBuf)msg;
        int count = buf.readableBytes();
        byte[] dst = new byte[count];
        buf.getBytes(0, dst);
        LOGGER.info("client.remote:" + ctx.channel().remoteAddress().toString() + "|client.content.hex:" + DatatypeConverter.printHexBinary(dst));

        final Channel proxy = ctx.channel();
        final Channel client = proxy.attr(k).get();
        boolean autoRead = client.isWritable() ? true : false;
        client.config().setAutoRead(autoRead);
        client.writeAndFlush(buf);
    }

    @Override
    public String getOwnerName() {
        return null;
    }

    @Override
    public List<LoadBalances.Key> decode(List<LoadBalances.Key> keys) {
        return keys;
    }
}
