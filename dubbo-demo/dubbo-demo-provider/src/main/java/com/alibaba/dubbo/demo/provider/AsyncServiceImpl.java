package com.alibaba.dubbo.demo.provider;

import com.alibaba.dubbo.demo.AsyncService;

/**
 * 异步执行
 *
 * @author dongxie
 * @date 10:12 2020-03-14
 */
public class AsyncServiceImpl implements AsyncService {

    @Override
    public String sayHello(String name) {
        System.out.println("async provider received: " + name);
        return "hello, " + name;
    }
}
