package com.dempe.forest.mqtt.interception.messages;

import com.dempe.forest.mqtt.support.proto.messages.PublishMessage;

import java.nio.ByteBuffer;

/**
 * @author Wagner Macedo
 */
public class InterceptPublishMessage extends InterceptAbstractMessage {
    private final PublishMessage msg;
    private final String clientID;

    public InterceptPublishMessage(PublishMessage msg, String clientID) {
        super(msg);
        this.msg = msg;
        this.clientID = clientID;
    }

    public String getTopicName() {
        return msg.getTopicName();
    }

    public ByteBuffer getPayload() {
        return msg.getPayload();
    }

    public String getClientID() {
        return clientID;
    }
}
