package com.alibaba.dubbo.demo;

import com.alibaba.dubbo.demo.model.ARequest;

/**
 * 只提供接口，没有实现类
 *
 * @author pengchao
 * @date 19:21 2020-07-27
 */
public interface TargetService {

    String sayhello(String request);

    String sayHello(ARequest request);

}
