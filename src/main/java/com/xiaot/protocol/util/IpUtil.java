package com.xiaot.protocol.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * <p>
 * IP工具类
 * </p>
 *
 * @author lzy
 * @since 2021/5/29.
 */
public class IpUtil {

    public static final String IP_REGEX = "((\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3})";
    private static volatile String cachedIpAddress;

    public static String getIp() {
        if (null != cachedIpAddress) {
            return cachedIpAddress;
        } else {
            Enumeration netInterfaces;
            try {
                netInterfaces = NetworkInterface.getNetworkInterfaces();
            } catch (SocketException var6) {
                throw new RuntimeException(var6);
            }

            String localIpAddress = null;

            while (netInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) netInterfaces.nextElement();
                Enumeration ipAddresses = netInterface.getInetAddresses();

                while (ipAddresses.hasMoreElements()) {
                    InetAddress ipAddress = (InetAddress) ipAddresses.nextElement();
                    if (isPublicIpAddress(ipAddress)) {
                        String publicIpAddress = ipAddress.getHostAddress();
                        cachedIpAddress = publicIpAddress;
                        return publicIpAddress;
                    }

                    if (isLocalIpAddress(ipAddress)) {
                        localIpAddress = ipAddress.getHostAddress();
                    }
                }
            }

            cachedIpAddress = localIpAddress;
            return localIpAddress;
        }
    }

    private static boolean isPublicIpAddress(InetAddress ipAddress) {
        return !ipAddress.isSiteLocalAddress() && !ipAddress.isLoopbackAddress() && !isV6IpAddress(ipAddress);
    }

    private static boolean isLocalIpAddress(InetAddress ipAddress) {
        return ipAddress.isSiteLocalAddress() && !ipAddress.isLoopbackAddress() && !isV6IpAddress(ipAddress);
    }

    private static boolean isV6IpAddress(InetAddress ipAddress) {
        return ipAddress.getHostAddress().contains(":");
    }

    public static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException var1) {
            throw new RuntimeException(var1);
        }
    }

    public static void main(String[] args) {
        System.out.println(getHostName());
        System.out.println(getIp());
    }

    private IpUtil() {
    }
}
