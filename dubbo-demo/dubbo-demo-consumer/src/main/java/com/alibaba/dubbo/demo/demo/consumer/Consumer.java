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

import com.alibaba.dubbo.demo.AsyncService;
import com.alibaba.dubbo.demo.DemoService;
import com.alibaba.dubbo.rpc.RpcContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Consumer {

    public static void main(String[] args) {
        //Prevent to get IPV6 address,this way only work in debug mode
        //But you can pass use -Djava.net.preferIPv4Stack=true,then it work well whether in debug mode or not
        System.setProperty("java.net.preferIPv4Stack", "true");
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"META-INF/spring/dubbo-demo-consumer.xml"});
        context.start();

        //范化调用
//        GenericService barService = (GenericService) context.getBean("demoService");
//        Object result = barService.$invoke("sayHello", new String[] { "java.lang.String" }, new Object[] { "World" });




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


        //异步调用
//        final AsyncService asyncService = (AsyncService) context.getBean("asyncService");

        while (true) {
            try {

//                RpcContext.getContext().setAttachment("secret","有内鬼");

                ExecutorService executorService = Executors.newFixedThreadPool(10);


                for(int i=0;i<10;i++) {
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            String hello = demoService.sayHello("world"); // call remote method
                            System.out.println(hello); // get result
                        }
                    });
                }



//                List<String> list = demoService.groupList(); // 分组聚合
//                System.out.println(list);





//                Future<String> result = RpcContext.getContext().asyncCall(new Callable<String>() {
//                    @Override
//                    public String call() throws Exception {
//                        return asyncService.sayHello("async call request");
//                    }
//                });

//                System.out.println("async result"+result.get());





                Thread.sleep(10000);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

    }

}
