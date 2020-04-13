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

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Invocation;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Dubbo的限流算法使用了最简单的计数器算法，如果并发流量刚好在上个计数器最后一秒和下个计数器第一秒来临，也不能完全预防突发流量，
 * 所以推荐自己使用令牌桶算法或漏桶算法实现自定义限流Filter，并且也可以考虑分布式限流
 *
 *
 * 推荐：https://blog.csdn.net/tianyaleixiaowu/article/details/74942405
 *
 *
 */
public class DefaultTPSLimiter implements TPSLimiter {

    private final ConcurrentMap<String, StatItem> stats
            = new ConcurrentHashMap<String, StatItem>();//每个服务维护一个计数器

    @Override
    public boolean isAllowable(URL url, Invocation invocation) {
        int rate = url.getParameter(Constants.TPS_LIMIT_RATE_KEY, -1);
        long interval = url.getParameter(Constants.TPS_LIMIT_INTERVAL_KEY,
                Constants.DEFAULT_TPS_LIMIT_INTERVAL);
        //servicekey并没有和方法绑定，只能限流接口
        String serviceKey = url.getServiceKey();
        if (rate > 0) {
            StatItem statItem = stats.get(serviceKey);
            if (statItem == null) {
                stats.putIfAbsent(serviceKey,
                        new StatItem(serviceKey, rate, interval));
                statItem = stats.get(serviceKey);
            }
            return statItem.isAllowable();
        } else {
            StatItem statItem = stats.get(serviceKey);
            if (statItem != null) {
                stats.remove(serviceKey);
            }
        }

        return true;
    }

}
