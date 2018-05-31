package com.tpson.kuluagent.netty.server;

import java.lang.annotation.*;

/**
 * Created by Zhangka in 2018/04/24
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Server {
    String value() default "";
}
