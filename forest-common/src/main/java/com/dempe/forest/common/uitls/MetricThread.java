package com.dempe.forest.common.uitls;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 性能监控工具类
 *
 * @author : Dempe
 * @version 1.0 date : 2014/11/24
 */
public class MetricThread extends TimerTask implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MetricThread.class);

    private String name;

    private AtomicInteger ps = new AtomicInteger(0);

    public MetricThread(String name) {
        this.name = name;
        new Timer().scheduleAtFixedRate(this, 1000L, 1000L);
    }

    public void increment() {
        ps.incrementAndGet();
    }

    @Override
    public void run() {
        LOGGER.warn("[name=" + name + "], " + "[ps/s=" + ps.get() + "]");
        ps = new AtomicInteger(0);
    }
}