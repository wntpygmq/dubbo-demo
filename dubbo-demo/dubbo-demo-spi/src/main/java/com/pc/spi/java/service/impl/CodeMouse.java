package com.pc.spi.java.service.impl;

import com.pc.spi.java.service.Mouse;

/**
 * 编程鼠标
 *
 * @author dongxie
 * @date 10:29 2020-02-25
 */
public class CodeMouse implements Mouse {
    @Override
    public void use() {
        System.out.println("我在写代码！");
    }
}
