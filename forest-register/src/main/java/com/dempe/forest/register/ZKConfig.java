package com.dempe.forest.register;

import org.aeonbits.owner.Config;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/1/29
 * Time: 16:55
 * To change this template use File | Settings | File Templates.
 */
@Config.Sources("classpath:zk.properties")
public interface ZKConfig extends Config {

    @Key("conn.str")
    @DefaultValue("localhost:2181")
    String connStr();


}
