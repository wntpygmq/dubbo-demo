/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.dubbo.demo.consumer;

import com.alibaba.dubbo.demo.AsyncService;
import com.alibaba.dubbo.demo.DemoService;
import com.alibaba.dubbo.demo.callback.CallbackListener;
import com.alibaba.dubbo.demo.callback.CallbackService;
import com.alibaba.dubbo.demo.eventnotify.NotifyDemoService;
import com.alibaba.dubbo.rpc.RpcContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class CallBackConsumer {

    public static void main(String[] args) throws InterruptedException {
        //Prevent to get IPV6 address,this way only work in debug mode
        //But you can pass use -Djava.net.preferIPv4Stack=true,then it work well whether in debug mode or not
        System.setProperty("java.net.preferIPv4Stack", "true");
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"dubbo-consumer.xml"});
        context.start();

        //参数回调
        CallbackService demoService = (CallbackService) context.getBean("callbackService"); // get remote service proxy

        //事件通知
        NotifyDemoService notifyDemoService = (NotifyDemoService) context.getBean("notifyDemoService");
        NotifyImpl notify = (NotifyImpl) context.getBean("notify");


        //调用结果
        String name = notifyDemoService.get(2);
        System.out.println("result:"+name);


        //查看是否有回调，回调方法会向ret中设置值
        for (int i = 0; i < 10; i++) {
            if (!notify.ret.containsKey(2)) {
                Thread.sleep(200);
            } else {
                break;
            }
        }

        System.out.println(notify.ret.get(2));

//        while (true) {
//            try {
//                //这里只是通过代理对象调用远程provider的方法，但是传如listener,provider会通过调用listener.changed()回调客户端
//                String result = demoService.addListener("this is key", new CallbackListener() {
//                    //这是在provider中调用的
//                    @Override
//                    public void changed(String msg) {
//                        System.out.println("callback1 "+msg);
//                    }
//                }); // call remote method
//
//
//                System.out.println(result);
//
//                Thread.sleep(10000);
//            } catch (Throwable throwable) {
//                throwable.printStackTrace();
//            }
//        }


    }

}
