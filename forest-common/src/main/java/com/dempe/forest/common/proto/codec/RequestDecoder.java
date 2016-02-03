package com.dempe.forest.common.proto.codec;

import com.dempe.forest.common.proto.Request;
import com.dempe.forest.common.uitls.pack.Marshallable;
import com.dempe.forest.common.uitls.pack.Unpack;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/3
 * Time: 11:33
 * To change this template use File | Settings | File Templates.
 */
public class RequestDecoder extends AbstractDecoder {

    @Override
    public Marshallable decode(Unpack unpack) throws IOException {
        return new Request().unmarshal(unpack);
    }
}
