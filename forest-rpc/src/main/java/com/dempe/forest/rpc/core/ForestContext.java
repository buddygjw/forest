package com.dempe.forest.rpc.core;

import com.dempe.forest.rpc.transport.protocol.PacketData;

import java.util.HashMap;

public class ForestContext {

    private PacketData packetData;


    public PacketData getPacketData() {
        return packetData;
    }

    public void setPacketData(PacketData packetData) {
        this.packetData = packetData;
    }

    private HashMap<String, Object> attributes = new HashMap<String, Object>();


    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }
}
