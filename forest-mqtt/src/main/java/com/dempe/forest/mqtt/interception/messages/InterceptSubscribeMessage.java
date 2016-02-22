package com.dempe.forest.mqtt.interception.messages;

import com.dempe.forest.mqtt.support.proto.messages.AbstractMessage;
import com.dempe.forest.mqtt.spi.impl.subscriptions.Subscription;

/**
 * @author Wagner Macedo
 */
public class InterceptSubscribeMessage {
    private final Subscription subscription;

    public InterceptSubscribeMessage(Subscription subscription) {
        this.subscription = subscription;
    }

    public String getClientID() {
        return subscription.getClientId();
    }

    public AbstractMessage.QOSType getRequestedQos() {
        return subscription.getRequestedQos();
    }

    public String getTopicFilter() {
        return subscription.getTopicFilter();
    }
}
