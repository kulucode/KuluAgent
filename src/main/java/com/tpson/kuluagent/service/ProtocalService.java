package com.tpson.kuluagent.service;

import com.tpson.kuluagent.domain.Protocal;
import com.tpson.kuluagent.netty.loadbalance.LoadBalances;
import io.netty.buffer.ByteBuf;

import java.util.List;

/**
 * Created by Zhangka in 2018/04/17
 */
public interface ProtocalService extends BaseService<Protocal> {
    List<Protocal> getByName(String name);
    List<LoadBalances.Key> getKeys(String name, ByteBuf msg);
    LoadBalances.Key getKey(Protocal protocal, ByteBuf msg);
}
