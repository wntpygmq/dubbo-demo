package com.pc.spi.dubbo.service.impl;

import com.alibaba.dubbo.common.URL;
import com.pc.spi.dubbo.service.KeyBoard;

/**
 * 编程键盘
 *
 * @author dongxie
 * @date 10:29 2020-02-25
 */
public class CodeKeyBoard implements KeyBoard {

    public CodeKeyBoard() {
        System.out.println("CodeKeyBoard init");
    }

    @Override
    public void code(URL url) {
        System.out.println("我在写代码！");
    }
}
