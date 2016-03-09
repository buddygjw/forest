package com.dempe.forest.common.protocol.codec;

import com.dempe.forest.common.pack.Marshallable;
import com.dempe.forest.common.pack.Unpack;
import com.dempe.forest.common.protocol.Response;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/3
 * Time: 11:36
 * To change this template use File | Settings | File Templates.
 */
public class ResponseDecoder extends AbstractDecoder {
    @Override
    public Marshallable decode(Unpack unpack) throws IOException {
        return new Response().unmarshal(unpack);
    }
}
