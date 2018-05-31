package com.tpson.kuluagent.netty.server;

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Zhangka in 2018/04/12
 * 流量拥塞控制.
 */
@Component
public class FlowControlExecutor {
    private static final Logger logger = LoggerFactory.getLogger(FlowControlExecutor.class);
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private final ConcurrentLinkedQueue<Channel> queue = new ConcurrentLinkedQueue<>();

    public void addChannel(Channel channel) {
        queue.add(channel);
    }

    @PostConstruct
    public void start() {
        executor.execute(() -> {
            logger.info("流量拥塞控制线程启动成功.");
            for (;;) {
                final ConcurrentLinkedQueue<Channel> queue = this.queue;
                queue.forEach(channel -> {
                    if (!channel.isActive()) {
                        queue.remove(channel);
                    } else if (channel.isWritable() && !channel.config().isAutoRead()) {
                        channel.config().setAutoRead(true);
                    }
                });

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    logger.error("流量拥塞控制线程睡眠时出错", e);
                }
            }
        });
    }

    @PreDestroy
    public void stop() {
        executor.shutdown();
    }
}
