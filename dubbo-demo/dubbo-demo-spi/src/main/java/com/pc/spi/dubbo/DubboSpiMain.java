package com.pc.spi.dubbo;

import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.pc.spi.dubbo.service.Computer;
import com.alibaba.dubbo.common.URL;
import com.pc.spi.dubbo.service.KeyBoard;
import java.util.HashMap;
import java.util.Map;

/**
 * 通过名称指定具体实现类，可以默认指定实现类
 * 支持Ioc和Aop
 * 自适应扩展，dubbo启动时可以先不加载，通过运行时参数指定类加载
 * 支持缓存，各种加载器，类文件都会被缓存
 *
 *
 * 下面三种测试，必须注释另外两种
 *
 *
 *
 * @author dongxie
 * @date 10:19 2020-02-25
 */
public class DubboSpiMain {

    public static void main(String[] args) {
        System.out.println("===============指定加载=================");
        //通过名称指定加载的实现类
        ExtensionLoader<KeyBoard> keyBoardLoader = ExtensionLoader.getExtensionLoader(KeyBoard.class);
        KeyBoard codeKeyBoard = keyBoardLoader.getExtension("code");//调用构造器初始化名为code的实例
//        codeKeyBoard.code(null);//调用实例方法
//
//        keyBoardLoader.getExtension("game").code(null);
//
//        System.out.println("===============IOC&AOP================");
        //实现Ioc和aop
        //aop在文件中配置wrapper类即可
//        ExtensionLoader<Computer> computerLoader = ExtensionLoader.getExtensionLoader(Computer.class);
//        //需要通过url来告诉Computer注入哪个实现类
//        Map<String,String> map = new HashMap<String, String>();
//        map.put("type","code");
//        URL url = new URL("","",0,map);
//        Computer computer = computerLoader.getExtension("mac");
//        computer.use(url);

        System.out.println("==============自适应扩展===============");

        /*
         * 自适应扩展：底层使用javassist,动态生成代码，最后还是通过ExtensionLoader.getExtensionLoader(Compiler.class)来
         * 实现。这样做可以在dubbo启动时先不加载类，而是在调用方法时根据运行时参数进行选择加载。
         */
        ExtensionLoader<KeyBoard> loader = ExtensionLoader.getExtensionLoader(KeyBoard.class);
        Map<String,String> param = new HashMap<String, String>();
        param.put("type","game");
        URL url1 = new URL("","",0,param);
        //此时还没有加载具体的实现类，只是生成了一个自适应的代理对象，自适应代理对象的code方法包含url解析和spi加载
        KeyBoard keyBoard$Adaptive = loader.getAdaptiveExtension();
        //代理对象的code方法会先根据url拿到extName并使用spi加载具体的配置文件中的实现类，并调用该实现类的code方法
        keyBoard$Adaptive.code(url1);


    }
}
