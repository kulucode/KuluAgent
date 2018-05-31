package com.tpson.kuluagent.domain;

import com.tpson.kuluagent.exception.ParamRuntimeException;
import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by Zhangka in 2018/04/17
 */

public class Protocal implements Serializable {
    private String name;
    private String startFlag;
    private String endFlag;
    private String split;
    private Integer offset;
    private Integer length;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.trim();
    }

    public String getStartFlag() {
        return startFlag;
    }

    public void setStartFlag(String startFlag) {
        this.startFlag = startFlag.trim();
    }

    public String getEndFlag() {
        return endFlag;
    }

    public void setEndFlag(String endFlag) {
        this.endFlag = endFlag.trim();
    }

    public String getSplit() {
        return split;
    }

    public void setSplit(String split) {
        this.split = split.trim();
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public void check() {
        if (StringUtils.isBlank(name))
            throw new ParamRuntimeException("name不能为空.");
        if (StringUtils.isBlank(startFlag))
            throw new ParamRuntimeException("startFlag不能为空.");
        if (StringUtils.isBlank(endFlag))
            throw new ParamRuntimeException("endFlag不能为空.");
        if (StringUtils.isBlank(split) && offset == null)
            throw new ParamRuntimeException("split和offset不能同时为空.");
        if (offset != null && offset < startFlag.length())
            throw new ParamRuntimeException("offset不能小于startFlag长度.");
        if (length == null || length <= 0)
            throw new ParamRuntimeException("length不能为空或必须大于0.");
    }

    public void check(ByteBuf msg) {
        this.check();

        if (msg == null || msg.readableBytes() <= startFlag.length() + endFlag.length())
            throw new ParamRuntimeException("消息体不能为空.");

        if (StringUtils.isNotBlank(split)) {
            String content = msg.toString(CharsetUtil.UTF_8);
            String[] array = content.split(split);
            if (array.length < length)
                throw new ParamRuntimeException("length越界.");
        } else if (msg.readableBytes() < offset) {
            throw new ParamRuntimeException("offset越界.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Protocal)) return false;
        Protocal that = (Protocal) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(startFlag, that.startFlag) &&
                Objects.equals(endFlag, that.endFlag) &&
                Objects.equals(split, that.split) &&
                Objects.equals(offset, that.offset) &&
                Objects.equals(length, that.length);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, startFlag, endFlag, split, offset, length);
    }

    @Override
    public String toString() {
        return "ProtocalVO{" +
                "name='" + name + '\'' +
                ", startFlag='" + startFlag + '\'' +
                ", endFlag='" + endFlag + '\'' +
                ", split='" + split + '\'' +
                ", offset=" + offset +
                ", length=" + length +
                '}';
    }
}
