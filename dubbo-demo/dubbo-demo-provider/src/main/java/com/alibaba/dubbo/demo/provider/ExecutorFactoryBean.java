package com.alibaba.dubbo.demo.provider;

import com.alibaba.dubbo.demo.DemoService;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import java.lang.reflect.Array;

import javax.annotation.Resource;

/**
 * TODO
 *
 * @author pengchao
 * @date 19:25 2020-07-27
 */
public class ExecutorFactoryBean<T> implements MethodInterceptor, FactoryBean<T>, InitializingBean {

    /**
     * 接口
     */
    private Class<T> clazz;
    /**
     * 请求参数基类
     */
    private Class<?> baseClazz;

    /**
     * 非executor执行类
     */
    @Resource
    private DemoService service;
    /**
     * 对外提供的代理服务对象
     */
    private T proxy;


    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Object[] arguments = methodInvocation.getArguments();
        int length = Array.getLength(arguments);
        if (length == 0 || length > 1) {
            Assert.notNull(service, "service must be not null!");
            return methodInvocation.getMethod().invoke(service, arguments);
        }

        Object argument = arguments[0];
        if (argument == null || !baseClazz.isAssignableFrom(argument.getClass())) {
            Assert.notNull(service, "service must be not null!");
            return methodInvocation.getMethod().invoke(service, arguments);
        }

        return "executor doing";
    }

    @Override
    public T getObject() throws Exception {//Method threw 'java.lang.IllegalArgumentException' exception. Cannot evaluate com.sun.proxy.$Proxy7.toString()
        return proxy;
    }


    @Override
    public Class<?> getObjectType() {
        return clazz;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(clazz, "clazz must be not null!");
        Assert.notNull(baseClazz, "baseClazz must be not null!");
        proxy = ProxyFactory.getProxy(clazz, this);
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }

    public Class<?> getBaseClazz() {
        return baseClazz;
    }

    public void setBaseClazz(Class<?> baseClazz) {
        this.baseClazz = baseClazz;
    }



}
