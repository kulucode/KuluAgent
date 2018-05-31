package com.tpson.kuluagent.netty.server.watch;

import com.tpson.kuluagent.netty.loadbalance.LoadBalances;
import com.tpson.kuluagent.netty.server.KuluAgentServerHandler;
import io.netty.channel.ChannelHandler;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ChannelHandler.Sharable
public class KuluAgentWatchServerHandler extends KuluAgentServerHandler {
    @Override
    public String getOwnerName() {
        return "CloudRing_H02";
    }

    @Override
    public List<LoadBalances.Key> decode(List<LoadBalances.Key> keys) {
        return keys;
    }
}
