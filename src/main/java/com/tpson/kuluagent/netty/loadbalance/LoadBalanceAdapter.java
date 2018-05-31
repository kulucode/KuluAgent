package com.tpson.kuluagent.netty.loadbalance;

import com.tpson.kuluagent.domain.Backend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Zhangka in 2018/04/12
 */
public class LoadBalanceAdapter implements LoadBalance {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoadBalanceAdapter.class);
    final transient ReentrantLock lock = new ReentrantLock();
    private Map<String, CopyOnWriteArrayList<Backend>> map;

    public LoadBalanceAdapter() {
        map = new ConcurrentHashMap<>();
    }

    @Override
    public Backend loadBalance(LoadBalances.Key key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Backend loadBalance(String ownerName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addBackend(Backend newBackend) {
        final ReentrantLock lock = this.lock;

        lock.lock();
        try {
            addBackendInternal(newBackend);
        } finally {
            lock.unlock();
        }
        LOGGER.info(map.toString());
    }

    @Override
    public void addBackend(Collection<? extends Backend> c) {
        final ReentrantLock lock = this.lock;

        lock.lock();
        try {
            c.forEach(backend -> addBackendInternal(backend));
        } finally {
            lock.unlock();
        }

        LOGGER.info(map.toString());
    }

    @Override
    public void removeBackend(Backend backend) {
        final Map<String, CopyOnWriteArrayList<Backend>> map = this.map;
        CopyOnWriteArrayList<Backend> list = map.get(backend.getProtocalName().toLowerCase());
        list.remove(backend);
    }

    @Override
    public void removeBackend(Collection<? extends Backend> c) {
        c.forEach(backend -> removeBackend(backend));
    }

    public Map<String, CopyOnWriteArrayList<Backend>> getMap() {
        return this.map;
    }

    public List<Backend> getBackends(String ownerName) {
        final Map<String, CopyOnWriteArrayList<Backend>> map = this.map;
        Optional<CopyOnWriteArrayList<Backend>> optional = map.entrySet().stream()
                .filter(e -> e.getKey().toLowerCase().startsWith(ownerName.toLowerCase()))
                .map(e -> e.getValue())
                .reduce((left, right) -> {
                    left.addAll(right);
                    return left;
                });
        return optional.isPresent() ? optional.get() : Collections.EMPTY_LIST;
    }

    protected void addBackendInternal(Backend backend) {
        final Map<String, CopyOnWriteArrayList<Backend>> map = this.map;
        String ownerName = backend.getProtocalName().toLowerCase();
        CopyOnWriteArrayList<Backend> list = map.get(ownerName);

        if (list == null) {
            list = new CopyOnWriteArrayList<>();
            list.add(backend);
            map.put(ownerName, list);
        } else {
            list.addIfAbsent(backend);
        }
    }
}
