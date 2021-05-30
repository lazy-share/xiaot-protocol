package com.xiaot.protocol.support;

import com.xiaot.protocol.custom.XiaotSecurityAuthProvide;
import com.xiaot.protocol.pojo.XiaotSecurity;

/**
 * <p>
 * 协议安全认证默认实现
 * </p>
 *
 * @author lzy
 * @since 2021/5/28.
 */
public class DefaultXiaotSecurityAuthProvide implements XiaotSecurityAuthProvide {

    @Override
    public String isAllow(XiaotSecurity security) {
        return null;
    }
}
