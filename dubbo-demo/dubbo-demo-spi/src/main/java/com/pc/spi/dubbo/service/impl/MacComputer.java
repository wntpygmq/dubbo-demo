package com.pc.spi.dubbo.service.impl;

import com.alibaba.dubbo.common.URL;
import com.pc.spi.dubbo.service.Computer;
import com.pc.spi.dubbo.service.KeyBoard;

/**
 * 测试IOC
 * 将键盘注入到电脑
 *
 * @author dongxie
 * @date 10:27 2020-02-25
 */
public class MacComputer implements Computer {

    private KeyBoard keyBoard;

    //只能通过setter注入
    public void setKeyBoard(KeyBoard keyBoard) {
        this.keyBoard = keyBoard;
    }

    @Override
    public void use(URL url) {
        keyBoard.code(url);
    }
}
