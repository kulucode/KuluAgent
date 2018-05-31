package com.tpson.kuluagent.util;

import org.springframework.beans.factory.BeanFactory;

/**
 *
 */
public final class SpringContextUtils {
    private static BeanFactory beanFactory;

    private SpringContextUtils() {
        throw new AssertionError("no com.tpson.kuluagent.util.SpringContextUtils instances for you!");
    }

    public static void setBeanFactory(BeanFactory factory) {
        beanFactory = factory;
    }

    public static <T> T getBean(Class<T> clazz) {
        return beanFactory.getBean(clazz);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return beanFactory.getBean(name, clazz);
    }
}
