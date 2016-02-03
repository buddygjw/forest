package com.dempe.forest.common.server;/*
 * Copyright (c) 2011 duowan.com. 
 * All Rights Reserved.
 * This program is the confidential and proprietary information of 
 * duowan. ("Confidential Information").  You shall not disclose such
 * Confidential Information and shall use it only in accordance with
 * the terms of the license agreement you entered into with duowan.com.
 */


import java.io.IOException;

/*

 */
public interface Server {

    /**
     * 启动服务器
     */
    public void start() throws IOException;

    /**
     * 停止服务器
     */
    public void stop() throws IOException;
}
