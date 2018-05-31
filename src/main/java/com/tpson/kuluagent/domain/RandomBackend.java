package com.tpson.kuluagent.domain;

/**
 * Created by Zhangka in 2018/04/12
 */
public class RandomBackend extends Backend {
    public RandomBackend() {}
    public RandomBackend(String ip, Integer port, String ownerName, String groupName) {
        super(ip, port, ownerName, groupName);
    }

    public RandomBackend(Backend b) {
        this(b.getIp(), b.getPort(), b.getProtocalName(), b.getGroupName());
    }

    @Override
    public String toString() {
        return "RandomBackend{" +
                "ip='" + getIp() + '\'' +
                ", port=" + getPort() +
                ", ownerName='" + getProtocalName() + '\'' +
                ", groupName='" + getGroupName() + '\'' +
                '}';
    }
}
