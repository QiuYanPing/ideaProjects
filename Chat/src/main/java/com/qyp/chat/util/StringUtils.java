package com.qyp.chat.util;

import cn.hutool.core.util.StrUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class StringUtils {
    public  String createSession(String userId, String contact) {
        String[] strings = new String[2];
        strings[0] = userId;
        strings[1] = contact;
        Arrays.sort(strings);
        return DigestUtils.md5Hex(StrUtil.join("",strings));
    }
}
