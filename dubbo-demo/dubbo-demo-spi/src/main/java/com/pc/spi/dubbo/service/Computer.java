package com.pc.spi.dubbo.service;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.SPI;

/**
 * 电脑
 *
 * @author dongxie
 * @date 10:21 2020-02-25
 */
@SPI
public interface Computer {
    void use(URL url);
}
