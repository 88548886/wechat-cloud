package com.mjoys;

import java.util.concurrent.TimeUnit;

public class SystemConstant {
    public static final int MAX_FRAME_LENGTH = 1024 * 1024;
    public static final int LENGTH_FIELD_LENGTH = 4;
    public static final int LENGTH_FIELD_OFFSET = 8;
    public static final int LENGTH_ADJUSTMENT = 0;
    public static final int INITIAL_BYTES_TO_STRIP = 0;

    public static final TimeUnit CHANNLE_IDLE_TIME_UNIT = TimeUnit.SECONDS;
    public static final int CHANNLE_IDLE_IIME = 5;

    public static String ip;
    public static int port;

    static {
        ip = System.getProperty("ip");
        String portStr = System.getProperty("port");
        if(null != portStr && (!"".equals(portStr)) ){
            try {
                SystemConstant.port = Integer.parseInt(portStr);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }
}
