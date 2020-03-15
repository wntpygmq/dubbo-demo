package com.alibaba.dubbo.demo.provider;

import com.alibaba.dubbo.demo.callback.CallbackListener;
import com.alibaba.dubbo.demo.callback.CallbackService;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 *
 * @author dongxie
 * @date 16:45 2020-03-14
 */
public class CallbackServiceImpl implements CallbackService {

    private final Map<String,CallbackListener> listeners = new ConcurrentHashMap<String, CallbackListener>();


    public CallbackServiceImpl() {
//        Thread t = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while(true) {
//                    try {
//                        for(Map.Entry<String, CallbackListener> entry : listeners.entrySet()){
//                            try {
//                                //调用每个listener的changed()
//                                entry.getValue().changed(getChanged(entry.getKey()));
//                            } catch (Throwable t) {
//                                listeners.remove(entry.getKey());
//                            }
//                        }
//                        Thread.sleep(10000); // 定时触发变更通知
//                    } catch (Throwable t) { // 防御容错
//                        t.printStackTrace();
//                    }
//                }
//            }
//
//
//        });
//        t.setDaemon(true);
//        t.start();


    }

    /**
     * consumer调用时，将传来的key和listener放入map中，并立即回调
     * @param key
     * @param listener
     */
    @Override
    public String addListener(String key, CallbackListener listener) {
        listeners.put(key,listener);
        listener.changed(getChanged(key));

        return "success";

    }

    private String getChanged(String key) {
        return "Changed: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"[{"+key+"}]";
    }
}
