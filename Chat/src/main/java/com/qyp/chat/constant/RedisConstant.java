package com.qyp.chat.constant;

public class RedisConstant {
    public static final String CHAT_WS_TOKEN = "chat:ws:token:";
    public static final Long CHAT_WS_TOKEN_EXPIRE = 60*60*24*2L; //2天

    public static final String CHAT_WS_USER_HEART_BEAT = "chat:ws:user:heartBeat:";
    public static final Long CHAT_WS_USER_HEART_BEAT_EXPIRES = 6L;

    public static final String CHAT_WS_USER_CONTACT = "chat:ws:user:contact:";

    public static final String CHAT_ACCOUNT_CHECK_CODE = "chat:account:checkCode:";
    public static final Long CHAT_ACCOUNT_CHECK_CODE_EXPIRES = 60L;

    public static final String CHAT_ADMIN_SYSTEM_SETTING = "chat:admin:systemSetting:";

    public static final String CHAT_BLOG_LIKE = "chat:blog:like:";
    public static final String CHAT_COMMENT_LIKE = "chat:comment:like:";

    public static final String CHAT_USER_SIGN = "chat:user:sign:";

}
