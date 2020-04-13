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
package com.alibaba.dubbo.rpc.filter.tps;

import java.util.concurrent.atomic.AtomicInteger;

class StatItem {

    private String name;

    private long lastResetTime;

    private long interval;//间隔时间

    private AtomicInteger token;//限流值

    private int rate;

    StatItem(String name, int rate, long interval) {
        this.name = name;
        this.rate = rate;
        this.interval = interval;
        this.lastResetTime = System.currentTimeMillis();
        this.token = new AtomicInteger(rate);
    }

    /**
     * 突刺现象，interval=10s，rate=1000，可是在10ms就超过1000，那么后面的9s990ms的时候就浪费了
     * @return
     */
    public boolean isAllowable() {
        long now = System.currentTimeMillis();
        if (now > lastResetTime + interval) {
            token.set(rate);//时间到了就恢复流量
            lastResetTime = now;
        }

        int value = token.get();
        boolean flag = false;
        while (value > 0 && !flag) {
            //并发减一
            flag = token.compareAndSet(value, value - 1);
            //获取新的值
            value = token.get();
        }

        return flag;
    }

    long getLastResetTime() {
        return lastResetTime;
    }

    int getToken() {
        return token.get();
    }

    @Override
    public String toString() {
        return new StringBuilder(32).append("StatItem ")
                .append("[name=").append(name).append(", ")
                .append("rate = ").append(rate).append(", ")
                .append("interval = ").append(interval).append("]")
                .toString();
    }

}
