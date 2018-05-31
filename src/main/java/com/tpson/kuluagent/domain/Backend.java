package com.tpson.kuluagent.domain;

import com.tpson.kuluagent.exception.ParamRuntimeException;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by Zhangka in 2018/04/12
 */
public class Backend implements Serializable {
    private String ip;
    private Integer port;

    /**
     * 所属系统名称.
     */
    private String protocalName;

    /**
     * 所属分组名称.
     */
    private String groupName;
    public Backend() {}

    public Backend(String ip, Integer port, String protocalName, String groupName) {
        this.ip = ip;
        this.port = port;
        this.protocalName = protocalName;
        this.groupName = groupName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getProtocalName() {
        return protocalName;
    }

    public void setProtocalName(String protocalName) {
        this.protocalName = protocalName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Backend)) return false;
        Backend backend = (Backend) o;
        return Objects.equals(ip, backend.ip) &&
                Objects.equals(port, backend.port) &&
                Objects.equals(protocalName, backend.protocalName) &&
                Objects.equals(groupName, backend.groupName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, port, protocalName, groupName);
    }

    @Override
    public String toString() {
        return "Backend{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                ", protocalName='" + protocalName + '\'' +
                ", groupName='" + groupName + '\'' +
                '}';
    }

    public void check() {
        if (StringUtils.isBlank(ip))
            throw new ParamRuntimeException("IP不能为空.");
        if (port == null || port <= 0)
            throw new ParamRuntimeException("PORT输入错误.");
        if (StringUtils.isBlank(protocalName))
            throw new ParamRuntimeException("协议不能为空.");
        if (StringUtils.isBlank(groupName))
            throw new ParamRuntimeException("分组不能为空.");
    }
}
