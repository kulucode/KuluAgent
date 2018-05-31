package com.tpson.kuluagent.service.impl;

import com.tpson.kuluagent.domain.RandomBackend;
import com.tpson.kuluagent.domain.WeightBackend;
import com.tpson.kuluagent.netty.loadbalance.WeightLoadBalance;
import com.tpson.kuluagent.service.WeightBackendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;

/**
 * Created by Zhangka in 2018/04/18
 */
@Service
public class WeightBackendServiceImpl extends RedisBaseServiceImpl<WeightBackend> implements WeightBackendService {
    @Autowired
    WeightLoadBalance weightLoadBalance;

    @Override
    public String getPrefix() {
        return "KULUAGENT_ZSET_WEIGHT_BACKEND";
    }

    @Override
    public Boolean add(WeightBackend newBackend) {
        Boolean ret = super.add(newBackend);
        if (ret) {
            weightLoadBalance.addBackend(newBackend);
        }
        return ret;
    }

    @Override
    public Long add(Collection<WeightBackend> c) {
        Long ret = super.add(c);
        if (ret > 0) {
            weightLoadBalance.addBackend(c);
        }
        return ret;
    }

    @Override
    public Long remove(WeightBackend newBackend) {
        Long ret = super.remove(newBackend);
        if (ret > 0) {
            weightLoadBalance.removeBackend(newBackend);
        }
        return ret;
    }

    @Override
    public Long remove(Collection<WeightBackend> c) {
        Long ret = super.remove(c);
        if (ret > 0) {
            weightLoadBalance.removeBackend(c);
        }
        return ret;
    }
}
