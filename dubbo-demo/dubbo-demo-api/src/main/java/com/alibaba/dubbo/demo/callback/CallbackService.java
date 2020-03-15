package com.alibaba.dubbo.demo.callback;

/**
 * 服务提供者
 *
 * @author dongxie
 * @date 16:39 2020-03-14
 */
public interface CallbackService {

    String addListener(String key, CallbackListener listener);
}
