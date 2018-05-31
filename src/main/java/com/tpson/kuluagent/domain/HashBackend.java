package com.tpson.kuluagent.domain;

import com.tpson.kuluagent.exception.ParamRuntimeException;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * Created by Zhangka in 2018/04/12
 */
public class HashBackend extends Backend {
    private String key;

    public HashBackend() {}
    public HashBackend(String ip, Integer port, String ownerName, String groupName) {
        super(ip, port, ownerName, groupName);
    }

    public HashBackend(Backend b) {
        this(b.getIp(), b.getPort(), b.getProtocalName(), b.getGroupName());
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HashBackend)) return false;
        if (!super.equals(o)) return false;
        HashBackend backend = (HashBackend) o;
        return Objects.equals(key, backend.key) &&
                super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), key);
    }

    @Override
    public String toString() {
        return "HashBackend{" +
                "key=" + key +
                "," + super.toString() +
                '}';
    }

    @Override
    public void check() {
        super.check();
        if (StringUtils.isBlank(key))
            throw new ParamRuntimeException("key不能为空.");
    }
}
