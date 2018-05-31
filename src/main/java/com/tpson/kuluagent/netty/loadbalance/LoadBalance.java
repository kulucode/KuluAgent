package com.tpson.kuluagent.netty.loadbalance;

import com.tpson.kuluagent.domain.Backend;

import java.util.Collection;

/**
 * Created by Zhangka in 2018/04/12
 */
public interface LoadBalance {
    Backend loadBalance(String ownerName);

    Backend loadBalance(LoadBalances.Key key);

    void addBackend(Backend backend);

    void addBackend(Collection<? extends Backend> c);

    void removeBackend(Backend backend);

    void removeBackend(Collection<? extends Backend> c);

//    boolean exists(T t);
}
