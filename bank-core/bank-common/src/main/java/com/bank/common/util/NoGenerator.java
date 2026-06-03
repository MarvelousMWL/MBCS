package com.bank.common.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.bank.common.constant.Constants;

public class NoGenerator {

    private static final Snowflake SNOWFLAKE = IdUtil.getSnowflake(1, 1);

    private NoGenerator() {}

    public static String generateCustomerNo(Long sequence) {
        return String.format("%09d", sequence);
    }

    public static String generateCustomerAccountNo(Long sequence) {
        String monthDay = cn.hutool.core.date.DateUtil.format(new java.util.Date(), "MMdd");
        String seq = String.format("%04d", sequence % 10000);
        return Constants.Institution.DEFAULT_INSTITUTION_NO + monthDay + seq;
    }

    public static String generateLiabilityAccountNo(Long sequence) {
        return String.valueOf(10000000L + sequence);
    }

    public static String generateTransactionNo() {
        return "TX" + SNOWFLAKE.nextIdStr();
    }
}
