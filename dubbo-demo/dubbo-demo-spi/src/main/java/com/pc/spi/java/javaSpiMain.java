package com.pc.spi.java;

import com.pc.spi.java.service.Mouse;
import java.util.ServiceLoader;

/**
 * java spi
 *
 *  不能单个指定获取具体的实现类，只能全部加载后遍历。
 *  不支持Ioc和Aop。
 *  不能延迟加载，就类必须先加载再使用。
 *  不支持缓存
 *
 * @author dongxie
 * @date 10:19 2020-02-25
 */
public class javaSpiMain {

    public static void main(String[] args) {

        ServiceLoader<Mouse> services = ServiceLoader.load(Mouse.class);

        for(Mouse mouse : services) {
            mouse.use();
        }

    }

}
