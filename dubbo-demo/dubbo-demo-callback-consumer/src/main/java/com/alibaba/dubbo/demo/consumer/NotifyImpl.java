package com.alibaba.dubbo.demo.consumer;

import com.alibaba.dubbo.demo.eventnotify.Notify;
import java.util.HashMap;
import java.util.Map;

/**
 * 服务消费者 Callback 实现
 *
 * @author dongxie
 * @date 18:01 2020-03-14
 */
public class NotifyImpl implements Notify {
    public Map<Integer, String> ret    = new HashMap<Integer, String>();
    public Map<Integer, Throwable> errors = new HashMap<Integer, Throwable>();

    @Override
    public void onreturn(String msg, Integer id) {
        System.out.println("onreturn:" + msg);
        ret.put(id, msg);
    }

    @Override
    public void onthrow(Throwable ex, Integer id) {
        errors.put(id, ex);
    }
}
