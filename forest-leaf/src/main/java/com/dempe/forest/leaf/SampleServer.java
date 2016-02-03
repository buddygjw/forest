package com.dempe.forest.leaf;

import com.dempe.forest.common.AppConfig;
import com.dempe.forest.common.server.BootServer;
import com.dempe.forest.common.server.Server;
import com.dempe.forest.common.uitls.DefConfigFactory;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 基于spring注解的sampleServer
 * User: Dempe
 * Date: 2016/1/28
 * Time: 15:41
 * To change this template use File | Settings | File Templates.
 */
@Configuration
@ComponentScan
public class SampleServer {

    public static void main(String[] args) throws Exception {
        // 启动spring容器
        ApplicationContext context = new AnnotationConfigApplicationContext(SampleServer.class);
        // 生成开发环境的配置
        AppConfig devConfig = DefConfigFactory.createDEVConfig();
        BootServer server = new BootServer(devConfig, context);
        server.registerNameService()
                .start();
    }
}
