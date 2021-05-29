package com.xiaot.protocol.custom;

import com.xiaot.protocol.pojo.XiaotSecurity;

/**
 * <p>
 * 协议安全提供接口定义
 * </p>
 *
 * @author lzy
 * @since 2021/5/28.
 */
public interface XiaotSecurityAuthProvide {


    boolean isAllow(XiaotSecurity security);

}
