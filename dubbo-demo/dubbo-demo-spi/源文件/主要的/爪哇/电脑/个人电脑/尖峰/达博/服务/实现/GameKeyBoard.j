package com.pc.spi.dubbo.service.impl;

import com.alibaba.dubbo.common.URL;
import com.pc.spi.dubbo.service.KeyBoard;

/**
 * 游戏键盘
 *
 * @author dongxie
 * @date 10:29 2020-02-25
 */
public class GameKeyBoard implements KeyBoard {

    public GameKeyBoard() {
        System.out.println("GameKeyBoard init");
    }

    @Override
    public void code(URL url) {
        System.out.println("我在打游戏！");
    }
}
