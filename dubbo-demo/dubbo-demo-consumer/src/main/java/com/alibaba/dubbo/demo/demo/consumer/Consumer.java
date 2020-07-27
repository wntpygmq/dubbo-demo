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
package com.alibaba.dubbo.demo.demo.consumer;

import com.alibaba.dubbo.demo.DemoService;
import com.alibaba.dubbo.demo.TargetService;
import com.alibaba.dubbo.demo.model.ARequest;
import com.alibaba.dubbo.rpc.RpcContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Consumer {


    private static ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        //Prevent to get IPV6 address,this way only work in debug mode
        //But you can pass use -Djava.net.preferIPv4Stack=true,then it work well whether in debug mode or not
        System.setProperty("java.net.preferIPv4Stack", "true");
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"META-INF/spring/dubbo-demo-consumer.xml"});
        context.start();

        //范化调用
//        GenericService barService = (GenericService) context.getBean("demoService");
//        Object result = barService.$invoke("sayHello", new String[] { "java.lang.String" }, new Object[] { "World" });



//
        final TargetService targetService = (TargetService) context.getBean("targetService");
        System.out.println(targetService.sayHello(new ARequest()));


        //demoService是proxy0对象
        /*
        public class proxy0 implements org.apache.dubbo.demo.DemoService {
            public static java.lang.reflect.Method[] methods;
            private java.lang.reflect.InvocationHandler handler;
            public proxy0() {
            }
            public proxy0(java.lang.reflect.InvocationHandler arg0) {
                handler = $1;
            }
            public java.lang.String sayHello(java.lang.String arg0) {
                Object[] args = new Object[1];
                args[0] = ($w) $1;
                Object ret = handler.invoke(this, methods[0], args);
                return (java.lang.String) ret;
            }
        }
         */
        final DemoService demoService = (DemoService) context.getBean("demoService"); // get remote service proxy

        int num = 0;
        while (true) {
            try {

                RpcContext.getContext().setAttachment("secret1","secret1");
                RpcContext.getContext().setAttachment("secret2","secret2");

                String hello = demoService.sayHello("world"+num++); // call remote method
                System.out.println(hello);



                //测试RpcContext隔离
//                for(int i=0;i<10;i++) {
//                    executorService.execute(new Runnable() {
//                        @Override
//                        public void run() {
//                            RpcContext.getContext().setAttachment("secret3","secret3");
//                            String hello = demoService.sayGood(); // call remote method
//                            System.out.println(hello); // get result
//                        }
//                    });
//                }





//                List<String> list = demoService.groupList(); // 分组聚合
//                System.out.println(list);




                //异步调用
//                final AsyncService asyncService = (AsyncService) context.getBean("asyncService");
//
//                for(int i=0;i<2;i++) {
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            String nullResult = asyncService.sayHello("future"+Thread.currentThread().getName());//返回null
//                            System.out.println(nullResult);
//
//                            try {
//                                //每个线程可以拿到自己的Future
//                                System.out.println("拿到future结果："+RpcContext.getContext().getFuture().get());
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    },i+"").start();
//                }







//                Future<String> result = RpcContext.getContext().asyncCall(new Callable<String>() {
//                    @Override
//                    public String call() throws Exception {
//                        return asyncService.sayHello("async call request");
//                    }
//                });

//                System.out.println("async result "+result.get());







                Thread.sleep(120*1000);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

    }

}
