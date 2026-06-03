package com.bank.common.util;

import cn.hutool.core.util.StrUtil;

public class Assert {

    private Assert() {}

    public static void notBlank(String str, String message) {
        if (StrUtil.isBlank(str)) {
            throw new com.bank.common.exception.BusinessException(message);
        }
    }

    public static void notNull(Object obj, String message) {
        if (obj == null) {
            throw new com.bank.common.exception.BusinessException(message);
        }
    }

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new com.bank.common.exception.BusinessException(message);
        }
    }

    public static void isFalse(boolean expression, String message) {
        if (expression) {
            throw new com.bank.common.exception.BusinessException(message);
        }
    }
}
