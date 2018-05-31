package com.tpson.kuluagent.netty.server.watch;

import com.tpson.kuluagent.netty.server.Server;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * 部标机转发服务.
 */
@Component
@Server("watch")
public class KuluAgentWatchServer {
    private static final Logger logger = LoggerFactory.getLogger(KuluAgentWatchServer.class);

    @Autowired
    private KuluAgentWatchServerHandler handler;
    @Autowired
    private WatchServerConfig config;

    private Channel channel;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    /**
     * 启动
     * @throws InterruptedException
     */
    @PostConstruct
    public void init() throws InterruptedException {
        logger.info("begin to start KuluAgent-Watch");
        bossGroup = new NioEventLoopGroup(config.getBoss());
        workerGroup = new NioEventLoopGroup(config.getWorker());

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, config.getBacklog())
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, config.getTimeout())
                //注意是childOption
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
//                .childOption(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(4 * 1024, 16 * 1024))
//                .childOption(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(4 * 1024, 16 * 1024))
//                .childOption(ChannelOption.SO_SNDBUF, 1024 * 256)
//                .childOption(ChannelOption.SO_RCVBUF, 1024 * 32768)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        socketChannel.pipeline()
//                                .addLast("decoder", new KuluWatchDecoder())
//                                .addLast("encoder", new KuluWatchEncoder())
                                .addLast(handler);
                    }
                });

        channel = serverBootstrap.bind(config.getPort()).sync().channel();
        logger.info("KuluAgent-Watch服务监听在" + config.getPort() + "端口");
    }

    @PreDestroy
    public void destory() {
        logger.info("destroy KuluAgent-Watch resources");
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
        if (channel != null) {
            channel.closeFuture().syncUninterruptibly();
        }
    }
}
