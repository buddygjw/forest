package com.dempe.forest.bus;

import com.dempe.forest.common.cluster.HAProxy;
import org.aeonbits.owner.Config;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/1/29
 * Time: 16:55
 * To change this template use File | Settings | File Templates.
 */
@Config.Sources("classpath:strategy.properties")
public interface StrategyConfig extends Config {

    @Key("load.strategy")
    @DefaultValue("DEFAULT")
    HAProxy.Strategy loadStrategy();

    @Key("period")
    @DefaultValue("1000")
    int period();

}
