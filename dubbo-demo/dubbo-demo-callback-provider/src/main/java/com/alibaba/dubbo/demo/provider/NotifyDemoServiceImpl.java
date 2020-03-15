package com.alibaba.dubbo.demo.provider;

import com.alibaba.dubbo.demo.eventnotify.NotifyDemoService;

/**
 * @author dongxie
 * @date 17:57 2020-03-14
 */
public class NotifyDemoServiceImpl implements NotifyDemoService {
    @Override
    public String get(int id) {
        if(1==id) {
            return "provider hahaha 1";
        } else {
            return "provider hahaha 2";
        }
    }
}
