package com.dempe.forest.mqtt.spi.impl.security;


import com.dempe.forest.mqtt.spi.security.IAuthenticator;

/**
 * Created by andrea on 8/23/14.
 */
public class AcceptAllAuthenticator implements IAuthenticator {
    public boolean checkValid(String username, byte[] password) {
        return true;
    }
}
