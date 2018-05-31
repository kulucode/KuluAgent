package com.tpson.kuluagent.service.impl;

import com.alibaba.fastjson.JSON;
import com.tpson.kuluagent.domain.Backend;
import com.tpson.kuluagent.domain.HashBackend;
import com.tpson.kuluagent.domain.Protocal;
import com.tpson.kuluagent.exception.ParamRuntimeException;
import com.tpson.kuluagent.netty.loadbalance.HashLoadBalance;
import com.tpson.kuluagent.netty.loadbalance.LoadBalances;
import com.tpson.kuluagent.netty.server.car.CarCodec;
import com.tpson.kuluagent.service.ProtocalService;
import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Zhangka in 2018/04/17
 */
@Service
public class ProtocalServiceImpl extends RedisBaseServiceImpl<Protocal> implements ProtocalService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProtocalServiceImpl.class);
    public static final String CAR_HEAD = "0x7e";
    public static final String CAR_TAIL = "0x7e";
    public static final String DEFAULT_PROTOCAL_NAME = "default";
    @Autowired
    HashLoadBalance hashLoadBalance;

    @Override
    public List<Protocal> getByName(String name) {
        Set<Protocal> protocals = super.all();
        if (protocals.size() == 0)
            return null;

        return protocals.stream().filter(p -> p.getName().toLowerCase().startsWith(name.toLowerCase())).collect(Collectors.toList());
    }

    @Override
    public LoadBalances.Key getKey(Protocal p, ByteBuf msg) {
        // 默认协议不解析
        if (p == null)
            return LoadBalances.Key.emptyKey();

        p.check(msg);
        // 处理粘包和分包
        /*msg = sticky(p, msg);
        if (StringUtils.isBlank(msg))
            throw new ParamRuntimeException("消息格式错误.");*/

        LoadBalances.Key key;
        if (StringUtils.isNotBlank(p.getSplit())) {
            String content = msg.toString(CharsetUtil.UTF_8);
            key = new LoadBalances.Key(content.split(",")[p.getLength() - 1], p.getName());
        } else {
            byte[] dst = new byte[p.getLength()];
            msg.getBytes(p.getOffset(), dst, 0, p.getLength());
            key = new LoadBalances.Key(dst, p.getName());
//            key = msg.substring(p.getOffset(), p.getOffset() + p.getLength());
        }

        return key;
    }

    @Override
    public List<LoadBalances.Key> getKeys(String ownerName, ByteBuf msg) {
        if (msg == null)
            return null;

        List<Protocal> ps = this.getByName(ownerName);
        if (ps.isEmpty())
            return Collections.EMPTY_LIST;// 默认协议

        List<LoadBalances.Key> keys = new ArrayList<>();
        for (Protocal p : ps) {
            try {
                LoadBalances.Key key = this.getKey(p, msg);// 是否抛出异常
                keys.add(key);
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
        }

        // 有协议，但是解析不出key，可能消息不完整
        // keys == null, 返回，继续接收消息
        return keys.isEmpty() ? null : keys;
    }

    @Override
    public String getPrefix() {
        return "KULUAGENT_ZSET_PROTOCAL";
    }

    // 处理粘包和分包
    protected String sticky(Protocal p, String msg) {
        String startFlag = p.getStartFlag();
        String endFlag = p.getEndFlag();

        if (startFlag.startsWith("0x")) {
            startFlag = startFlag.substring(2);
        }
        if (endFlag.startsWith("0x")) {
            endFlag = endFlag.substring(2);
        }

        startFlag = new String(DatatypeConverter.parseHexBinary(startFlag));
        endFlag = new String(DatatypeConverter.parseHexBinary(endFlag));

        /*if (msg.contains(startFlag + startFlag))
            msg.replaceAll(startFlag + startFlag, startFlag);
        if (msg.contains(endFlag + endFlag))
            msg.replaceAll(endFlag + endFlag, endFlag);
        if (msg.startsWith(startFlag) && msg.endsWith(endFlag))
            return msg;*/

        int startIndex = msg.indexOf(startFlag);
        int endIndex = msg.indexOf(endFlag, startIndex + startFlag.length());

        if (startIndex == -1 || endIndex == -1)
            return null;

        String ret =  msg.substring(startIndex, endIndex + endFlag.length());
        if (CAR_HEAD.equalsIgnoreCase(startFlag) && CAR_TAIL.equalsIgnoreCase(endFlag)) {
            ret = new String(CarCodec.decode(ret.getBytes()));
        }
        return ret;
    }
}
