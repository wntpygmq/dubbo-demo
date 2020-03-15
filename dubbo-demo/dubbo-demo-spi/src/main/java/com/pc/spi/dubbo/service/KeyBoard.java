package com.pc.spi.dubbo.service;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.Adaptive;
import com.alibaba.dubbo.common.extension.SPI;

/**
 * 键盘
 *
 * @author dongxie
 * @date 10:26 2020-02-25
 */
@SPI//这里可以指定默认实现类
public interface KeyBoard {
    @Adaptive(value = "type")//通过url中的map参数中key为type的值获取注入的实现类
    void code(URL url);
}

//生成如下代码
/*
package com.pc.spi.dubbo.service;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
public class KeyBoard$Adaptive implements com.pc.spi.dubbo.service.KeyBoard {
    public void code(com.alibaba.dubbo.common.URL arg0) {
        if (arg0 == null)
            throw new IllegalArgumentException("url == null");
        com.alibaba.dubbo.common.URL url = arg0;
        String extName = url.getParameter("type");
        if(extName == null)
            throw new IllegalStateException("Fail to get extension(com.pc.spi.dubbo.service.KeyBoard) name from url(" + url.toString() + ") use keys([type])");
        com.pc.spi.dubbo.service.KeyBoard extension = (com.pc.spi.dubbo.service.KeyBoard)ExtensionLoader
                .getExtensionLoader(com.pc.spi.dubbo.service.KeyBoard.class)
                .getExtension(extName);
        extension.code(arg0);
    }
}
*/
