package com.qyp.chat.constant;

public class RedisConstant {
    public static final String CHAT_WS_TOKEN = "chat:ws:token:";
    public static final Long CHAT_WS_TOKEN_EXPIRE = 60*60*24*2L; //2天

    public static final String CHAT_WS_USER_HEART_BEAT = "chat:ws:user:heartBeat:";
    public static final Long CHAT_WS_USER_HEART_BEAT_EXPIRES = 6L;

    public static final String CHAT_ACCOUNT_CHECK_CODE = "chat:account:checkCode:";
    public static final Long CHAT_ACCOUNT_CHECK_CODE_EXPIRES = 60L;
}
