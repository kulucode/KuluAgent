package com.tpson.kuluagent.netty.loadbalance;

import com.tpson.kuluagent.domain.Backend;
import com.tpson.kuluagent.domain.RandomBackend;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Zhangka in 2018/04/12
 */
@Component
public class RandomLoadBalance extends LoadBalanceAdapter implements LoadBalance {
    @Override
    public Backend loadBalance(String ownerName) {
        final List<Backend> list = getBackends(ownerName);
        if (list == null || list.size() == 0)
            return null;

        int idx = ThreadLocalRandom.current().nextInt(list.size());     //[0, backends.size)
        return list.get(idx);
    }
}
