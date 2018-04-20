package com.mjoys;

public class ServerNode {
    private String host;
    private int port;

    public String getHost() {
        return host;
    }

    public ServerNode setHost(String host) {
        this.host = host;
        return this;
    }

    public int getPort() {
        return port;
    }

    public ServerNode setPort(int port) {
        this.port = port;
        return this;
    }
}
