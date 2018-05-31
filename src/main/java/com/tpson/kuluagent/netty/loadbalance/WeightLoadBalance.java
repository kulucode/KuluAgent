package com.tpson.kuluagent.netty.loadbalance;

import com.tpson.kuluagent.domain.Backend;
import com.tpson.kuluagent.domain.WeightBackend;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Zhangka in 2018/04/12
 */
@Component
public class WeightLoadBalance extends LoadBalanceAdapter implements LoadBalance {
    @Override
    public Backend loadBalance(String ownerName) {
        final List<Backend> list = getBackends(ownerName);
        if (list == null || list.size() == 0)
            return null;

        int totalWeight = list.stream().mapToInt(w -> ((WeightBackend)w).getWeight()).sum();
        int offset = ThreadLocalRandom.current().nextInt(totalWeight);          //[0, totalWeight)

        // 并确定随机值落在哪个片断上
        for (int i = 0; i < list.size(); ++i) {
            WeightBackend backend = (WeightBackend)list.get(i);
            offset -= backend.getWeight();
            if (offset < 0) {
                return list.get(i);
            }
        }
        return null;
    }
}
