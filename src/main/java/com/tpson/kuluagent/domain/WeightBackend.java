package com.tpson.kuluagent.domain;

import com.tpson.kuluagent.exception.ParamRuntimeException;

/**
 * Created by Zhangka in 2018/04/12
 */
public class WeightBackend extends Backend {
    private Integer weight;

    public WeightBackend() {}
    public WeightBackend(String ip, Integer port, String ownerName, String groupName, Integer weight) {
        super(ip, port, ownerName, groupName);
        this.weight = weight;
    }

    public WeightBackend(Backend b, Integer weight) {
        this(b.getIp(), b.getPort(), b.getProtocalName(), b.getGroupName(), weight);
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "WeightBackend{" +
                "weight=" + weight +
                "," + super.toString() +
                '}';
    }

    @Override
    public void check() {
        super.check();
        if (weight == null || weight <= 0)
            throw new ParamRuntimeException("weight必须大于0.");
    }
}
