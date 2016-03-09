package com.dempe.forest.register;

import org.aeonbits.owner.ConfigFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/1/29
 * Time: 16:58
 * To change this template use File | Settings | File Templates.
 */
public class ZKClientFactory {

    public static CuratorFramework getZKClient() {
        ZKConfig cfg = ConfigFactory.create(ZKConfig.class);
        return CuratorFrameworkFactory.newClient(cfg.connStr(), new ExponentialBackoffRetry(1000, 3));
    }
}
