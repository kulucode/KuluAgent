package com.tpson.kuluagent.netty.server.car;

import com.tpson.kuluagent.netty.loadbalance.LoadBalances;
import com.tpson.kuluagent.netty.server.KuluAgentServerHandler;
import com.tpson.kuluagent.util.ConvertUtils;
import io.netty.channel.ChannelHandler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@ChannelHandler.Sharable
public class KuluAgentCarServerHandler extends KuluAgentServerHandler {

    @Override
    public String getOwnerName() {
        return "JTT808-2011";
    }

    @Override
    public List<LoadBalances.Key> decode(List<LoadBalances.Key> keys) {
        if (keys == null || keys.isEmpty())
            return keys;

        return keys.stream().map(k -> new LoadBalances.Key(ConvertUtils.bcd2Str(k.getBytes()), k.getProtocalName())).collect(Collectors.toList());
    }
}
