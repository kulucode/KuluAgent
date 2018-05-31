package com.tpson.kuluagent.domain;

import com.tpson.kuluagent.exception.ParamRuntimeException;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by Zhangka in 2018/04/17
 */
public class Group implements Serializable {
    private String name;
    private String comment;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Group)) return false;
        Group group1 = (Group) o;
        return Objects.equals(name, group1.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Group{" +
                "name='" + name + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }

    public void check() {
        if (StringUtils.isBlank(name))
            throw new ParamRuntimeException("name不能为空.");
    }
}
