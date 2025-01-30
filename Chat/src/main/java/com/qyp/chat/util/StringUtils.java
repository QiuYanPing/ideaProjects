package com.qyp.chat.util;

import cn.hutool.core.util.StrUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Locale;

@Component
public class StringUtils {
    public static boolean isNumber(String messageId) {
        return messageId.matches("^[0-9]*$");
    }

    public String createSession(String userId, String contact) {
        String[] strings = new String[2];
        strings[0] = userId;
        strings[1] = contact;
        Arrays.sort(strings);
        return DigestUtils.md5Hex(StrUtil.join("", strings));
    }

    public String cleanHtml(String sendMessage) {
        if (StrUtil.isEmpty(sendMessage))
            return sendMessage;
        sendMessage = sendMessage.replace("<", "&lt;");
        sendMessage = sendMessage.replace("\r\n", "<br>");
        sendMessage = sendMessage.replace("\n", "<br>");
        return sendMessage;
    }

    public String getSuffix(String fileName) {
        return fileName.substring(fileName.indexOf('.')).toLowerCase();
    }
}
