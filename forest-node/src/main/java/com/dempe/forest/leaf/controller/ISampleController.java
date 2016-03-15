package com.dempe.forest.leaf.controller;

import com.dempe.forest.leaf.service.SampleService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * sample业务开发controller
 * User: Dempe
 * Date: 2016/1/28
 * Time: 15:42
 * To change this template use File | Settings | File Templates.
 */
@Component
public class ISampleController implements SampleController {

    @Resource
    SampleService lampService;


    public String hello(String name) {
        return lampService.hello(name).toJSONString();
    }
}
