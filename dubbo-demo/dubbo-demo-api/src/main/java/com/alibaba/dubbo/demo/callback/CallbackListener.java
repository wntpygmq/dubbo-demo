package com.alibaba.dubbo.demo.callback;

/**
 * 消费端向提供者传递该监听
 * 提供者通过调用监听的方法回调消费者逻辑
 *
 * @author dongxie
 * @date 16:39 2020-03-14
 */
public interface CallbackListener {
    void changed(String msg);
}
