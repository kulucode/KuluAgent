package com.tpson.kuluagent.netty.client;

import com.tpson.kuluagent.domain.Backend;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

@Component
public class KuluAgent {
    private static final Logger LOGGER = LoggerFactory.getLogger(KuluAgent.class);
    private static Bootstrap b;
    private static EventLoopGroup workerGroup;
    @Autowired
    KuluAgentHandler kuluAgentHandler;

    @PostConstruct
    public void init() {
        b = new Bootstrap();
        workerGroup = new NioEventLoopGroup();
        b.group(workerGroup);
        b.channel(NioSocketChannel.class);
        b.option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline()
//                        .addLast("decoder", new KuluWatchDecoder())
//                        .addLast("encoder", new KuluWatchEncoder())
                        .addLast(kuluAgentHandler);
            }
        });
    }

    @PreDestroy
    public void destory() {
        if (workerGroup != null && !workerGroup.isShutdown()) {
            LOGGER.info("workerGroup.shutdownGracefully()");
            workerGroup.shutdownGracefully();
            workerGroup = null;
        }
    }

    public Channel connect(String host, int port) {
        ChannelFuture f = null;
        try {
            f = b.connect(InetAddress.getByName(host).getHostAddress(), port);
        } catch (UnknownHostException e) {
            LOGGER.error("域名解析出错,host:" + host, e);
        }
        f.awaitUninterruptibly();

        assert f.isDone();

        Channel proxy;
        if (f.isCancelled()) {
            throw new RuntimeException("Connection attempt cancelled by user[host:" + host + ",port:" + port + "]");
        } else if (!f.isSuccess()) {
            f.cause().printStackTrace();
            throw new RuntimeException("failed to connect to server[host:" + host + ",port:" + port + "]");
        } else {
            proxy = f.channel();
        }

        return proxy;
    }

    public Channel connect(final Backend backend) {
        return this.connect(backend.getIp(), backend.getPort());
    }
}
