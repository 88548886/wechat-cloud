package com.mjoys;

public class Terminal {

    public static class Addr{
        private String ip;
        private int port;

        public String getIp() {
            return ip;
        }

        public Addr setIp(String ip) {
            this.ip = ip;
            return this;
        }

        public int getPort() {
            return port;
        }

        public Addr setPort(int port) {
            this.port = port;
            return this;
        }
    }
}
