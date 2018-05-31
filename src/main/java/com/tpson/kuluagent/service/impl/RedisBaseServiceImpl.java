package com.tpson.kuluagent.service.impl;

import com.tpson.kuluagent.exception.RepeatRuntimeException;
import com.tpson.kuluagent.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Zhangka in 2018/04/18
 */
public abstract class RedisBaseServiceImpl<T> implements BaseService<T> {
    private static final String REPEAT_ADD_MSG = "重复添加";
    @Autowired
    RedisTemplate<String, T> redisTemplate;

    @Override
    public Boolean add(T t) {
        Set<T> s = all();
        if (s.contains(t))
            throw new RepeatRuntimeException(REPEAT_ADD_MSG);

        return redisTemplate.opsForZSet().add(getPrefix(), t, System.currentTimeMillis());
    }

    @Override
    public Long add(Collection<T> c) {
        Set<ZSetOperations.TypedTuple<T>> tuples = new HashSet<>();
        Set<T> s = all();
        double times = System.currentTimeMillis();
        c.forEach(e -> {
            if (!s.contains(e))
                tuples.add(new DefaultTypedTuple<>(e, times));
        });

        if (tuples.isEmpty())
            throw new RepeatRuntimeException(REPEAT_ADD_MSG);

        return redisTemplate.opsForZSet().add(getPrefix(), tuples);
    }

    @Override
    public Long remove(T t) {
        return redisTemplate.opsForZSet().remove(getPrefix(), t);
    }

    @Override
    public Long remove(Collection<T> c) {
        return redisTemplate.opsForZSet().remove(getPrefix(), c.toArray());
    }

    @Override
    public Set<T> page(Integer offset, Integer limit) {
        return redisTemplate.opsForZSet().reverseRange(getPrefix(), offset, offset + limit);
    }

    @Override
    public Long count() {
        return redisTemplate.opsForZSet().size(getPrefix());
    }

    @Override
    public Set<T> all() {
        return redisTemplate.opsForZSet().reverseRange(getPrefix(), 0, -1);
    }

    public abstract String getPrefix();
}
