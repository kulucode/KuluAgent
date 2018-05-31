package com.tpson.kuluagent.netty.loadbalance;

import com.tpson.kuluagent.domain.Backend;
import com.tpson.kuluagent.domain.HashBackend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * Created by Zhangka in 2018/04/12
 */
@Component
public class HashLoadBalance extends LoadBalanceAdapter implements LoadBalance {
    private static final Logger LOGGER = LoggerFactory.getLogger(HashLoadBalance.class);
    @Override
    public Backend loadBalance(LoadBalances.Key key) {
        String ownerName = key.getProtocalName();
        final List<Backend> list = getBackends(ownerName);

        if (list == null || list.size() == 0)
            return null;

        List<Backend> hit = list.stream()
                .filter(hashBackend -> ((HashBackend)hashBackend).getKey().equalsIgnoreCase(key.getKey()) && hashBackend.getProtocalName().equalsIgnoreCase(key.getProtocalName()))
                .collect(Collectors.toList());
        if (hit.size() == 0) {
            return null;
        } else if (hit.size() == 1) {
            return hit.get(0);
        } else {
            int idx = ThreadLocalRandom.current().nextInt(hit.size());     //[0, backends.size)
            return hit.get(idx);
        }
    }
}
