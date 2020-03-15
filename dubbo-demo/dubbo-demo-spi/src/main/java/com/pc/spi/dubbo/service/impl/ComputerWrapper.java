package com.pc.spi.dubbo.service.impl;

import com.alibaba.dubbo.common.URL;
import com.pc.spi.dubbo.service.Computer;

/**
 * 测试Aop
 * 电脑包装类,相当于静态代理
 *
 * @author dongxie
 * @date 10:45 2020-02-25
 */
public class ComputerWrapper implements Computer {

    private Computer computer;

    public ComputerWrapper(Computer computer) {
        this.computer = computer;
    }

    @Override
    public void use(URL url) {
        System.out.println("插上键盘");
        computer.use(url);
        System.out.println("拔掉键盘");
    }
}
