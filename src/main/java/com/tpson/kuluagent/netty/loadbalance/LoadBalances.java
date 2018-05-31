package com.tpson.kuluagent.netty.loadbalance;

import com.tpson.kuluagent.domain.*;
import com.tpson.kuluagent.service.HashBackendService;
import com.tpson.kuluagent.service.ProtocalService;
import com.tpson.kuluagent.service.RandomBackendService;
import com.tpson.kuluagent.service.WeightBackendService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Zhangka in 2018/04/16
 */
@Component
public class LoadBalances {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoadBalances.class);

    @Autowired
    private HashLoadBalance hashLoadBalance;
    @Autowired
    private WeightLoadBalance weightLoadBalance;
    @Autowired
    private RandomLoadBalance randomLoadBalance;

    @Autowired
    RandomBackendService randomBackendService;
    @Autowired
    WeightBackendService weightBackendService;
    @Autowired
    HashBackendService hashBackendService;

    public Backend loadBalance(String ownerName, Key... keys) {
        if (StringUtils.isBlank(ownerName) && keys.length == 0)
            return null;

        Backend backend = null;
        if (keys != null && keys.length > 0) {
            List<Backend> hits = new ArrayList<>();
            for (Key key : keys) {
                backend = hashLoadBalance.loadBalance(key);
                if (backend != null)
                    hits.add(backend);
            }

            if (hits.size() > 0) {
                int idx = ThreadLocalRandom.current().nextInt(hits.size());     //[0, hits.size)
                backend = hits.get(idx);
            }
        }
        if (backend == null) {
            backend = weightLoadBalance.loadBalance(ownerName);
        }
        if (backend == null) {
            backend = randomLoadBalance.loadBalance(ownerName);
        }

        return backend;
    }

    /**
     * 初始化负载均衡器.
     */
    @PostConstruct
    public void initialize() {
        randomInitialize();
        weightInitialize();
        hashInitialize();
        LOGGER.info(hashLoadBalance.getMap().toString());
    }


    public void randomInitialize() {
        Set<RandomBackend> set = randomBackendService.all();
        if (!set.isEmpty())
            randomLoadBalance.addBackend(set);
    }

    public void weightInitialize() {
        Set<WeightBackend> set = weightBackendService.all();
        if (!set.isEmpty())
            weightLoadBalance.addBackend(set);
    }

    public void hashInitialize() {
        Set<HashBackend> set = hashBackendService.all();
        if (!set.isEmpty())
            hashLoadBalance.addBackend(set);
    }

    public static class Key {
        private String key;
        private String protocalName;
        private byte[] bytes;

        public Key(String key, String protocalName) {
            this.key = key;
            this.protocalName = protocalName;
        }

        public Key(byte[] bytes, String protocalName) {
            this.bytes = bytes;
            this.protocalName = protocalName;
        }

        public Key(String key, String protocalName, byte[] bytes) {
            this.key = key;
            this.protocalName = protocalName;
            this.bytes = bytes;
        }

        public static Key emptyKey() {
            return new Key(null, null, null);
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getProtocalName() {
            return protocalName;
        }

        public void setProtocalName(String protocalName) {
            this.protocalName = protocalName;
        }

        public byte[] getBytes() {
            return bytes;
        }

        public void setBytes(byte[] bytes) {
            this.bytes = bytes;
        }

        @Override
        public String toString() {
            return "Key{" +
                    "key='" + key + '\'' +
                    ", protocalName='" + protocalName + '\'' +
                    '}';
        }
    }
}
