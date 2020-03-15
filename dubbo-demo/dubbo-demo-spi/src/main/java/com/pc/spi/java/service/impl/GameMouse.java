package com.pc.spi.java.service.impl;

import com.pc.spi.java.service.Mouse;

/**
 * 游戏鼠标
 *
 * @author dongxie
 * @date 10:29 2020-02-25
 */
public class GameMouse implements Mouse {
    @Override
    public void use() {
        System.out.println("我在打游戏！");
    }
}
