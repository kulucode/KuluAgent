package com.tpson.kuluagent.service;

import java.util.Collection;
import java.util.Set;

/**
 * Created by Zhangka in 2018/04/18
 */
public interface BaseService<T> {
    Boolean add(T t);

    Long add(Collection<T> c);

    Long remove(T t);

    Long remove(Collection<T> c);

    Set<T> page(Integer offset, Integer limit);

    Long count();

    Set<T> all();
}
