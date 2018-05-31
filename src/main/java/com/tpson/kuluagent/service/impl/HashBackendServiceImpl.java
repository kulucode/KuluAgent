package com.tpson.kuluagent.service.impl;

import com.tpson.kuluagent.domain.HashBackend;
import com.tpson.kuluagent.netty.loadbalance.HashLoadBalance;
import com.tpson.kuluagent.service.HashBackendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Created by Zhangka in 2018/04/18
 */
@Service
public class HashBackendServiceImpl extends RedisBaseServiceImpl<HashBackend> implements HashBackendService {
    @Autowired
    HashLoadBalance hashLoadBalance;

    @Override
    public String getPrefix() {
        return "KULUAGENT_ZSET_HASH_BACKEND";
    }

    @Override
    public Boolean add(HashBackend newBackend) {
        Boolean ret = super.add(newBackend);
        if (ret) {
            hashLoadBalance.addBackend(newBackend);
        }
        return ret;
    }

    @Override
    public Long add(Collection<HashBackend> c) {
        Long ret = super.add(c);
        if (ret > 0) {
            hashLoadBalance.addBackend(c);
        }
        return ret;
    }

    @Override
    public Long remove(HashBackend newBackend) {
        Long ret = super.remove(newBackend);
        if (ret > 0) {
            hashLoadBalance.removeBackend(newBackend);
        }
        return ret;
    }

    @Override
    public Long remove(Collection<HashBackend> c) {
        Long ret = super.remove(c);
        if (ret > 0) {
            hashLoadBalance.removeBackend(c);
        }
        return ret;
    }
}
