package com.dempe.forest.mqtt.support.netty;

import com.dempe.forest.mqtt.support.proto.messages.ConnAckMessage;
import io.netty.buffer.ByteBuf;
import io.netty.util.AttributeMap;

import java.util.List;

/**
 *
 * @author andrea
 */
class ConnAckDecoder extends DemuxDecoder {

    @Override
    void decode(AttributeMap ctx, ByteBuf in, List<Object> out) throws Exception {
        in.resetReaderIndex();
        //Common decoding part
        ConnAckMessage message = new ConnAckMessage();
        if (!decodeCommonHeader(message, 0x00, in)) {
            in.resetReaderIndex();
            return;
        }
        //skip reserved byte
        in.skipBytes(1);
        
        //read  return code
        message.setReturnCode(in.readByte());
        out.add(message);
    }
    
}
