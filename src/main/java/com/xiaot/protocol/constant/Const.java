package com.xiaot.protocol.constant;

import com.xiaot.protocol.util.IpUtil;

/**
 * <p>
 *
 * </p>
 *
 * @author lzy
 * @since 2021/5/27.
 */
public class Const {

    public static final int FAIL = 0;
    public static final int SUCCESS = 1;
    public static final int MAJOR = 0xACAFDCBA;
    public static final char MAIN_VERSION = '1';
    public static final char MINOR_VERSION = '1';
    public static final String CLIENT_LOCAL_IP = IpUtil.getIp();
    public static final int CLIENT_LOCAL_PORT = 12316;
    public static final int SERVER_PORT = 9000;

}
