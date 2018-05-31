package com.tpson.kuluagent.service.impl;

import com.tpson.kuluagent.domain.RandomBackend;
import com.tpson.kuluagent.netty.loadbalance.RandomLoadBalance;
import com.tpson.kuluagent.service.RandomBackendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Created by Zhangka in 2018/04/18
 */
@Service
public class RandomBackendServiceImpl extends RedisBaseServiceImpl<RandomBackend> implements RandomBackendService {
    @Autowired
    RandomLoadBalance randomLoadBalance;

    @Override
    public String getPrefix() {
        return "KULUAGENT_ZSET_RANDOM_BACKEND";
    }

    @Override
    public Boolean add(RandomBackend newBackend) {
        Boolean ret = super.add(newBackend);
        if (ret) {
            randomLoadBalance.addBackend(newBackend);
        }
        return ret;
    }

    @Override
    public Long add(Collection<RandomBackend> c) {
        Long ret = super.add(c);
        if (ret > 0) {
            randomLoadBalance.addBackend(c);
        }
        return ret;
    }

    @Override
    public Long remove(RandomBackend newBackend) {
        Long ret = super.remove(newBackend);
        if (ret > 0) {
            randomLoadBalance.removeBackend(newBackend);
        }
        return ret;
    }

    @Override
    public Long remove(Collection<RandomBackend> c) {
        Long ret = super.remove(c);
        if (ret > 0) {
            randomLoadBalance.removeBackend(c);
        }
        return ret;
    }
}
