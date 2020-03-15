package com.alibaba.dubbo.demo.eventnotify;

/**
 * 服务消费者 Callback 接口
 *
 * @author dongxie
 * @date 18:00 2020-03-14
 */
public interface Notify {
   void onreturn(String msg, Integer id);
   void onthrow(Throwable ex, Integer id);
}
