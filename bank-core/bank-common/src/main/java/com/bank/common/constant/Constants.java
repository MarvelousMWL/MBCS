package com.bank.common.constant;

public class Constants {

    private Constants() {}

    public static class Institution {
        public static final String DEFAULT_INSTITUTION_NO = "100000";
        private Institution() {}
    }

    public static class Sequence {
        public static final long CUSTOMER_NO_START = 1L;
        public static final long LIABILITY_ACCOUNT_NO_START = 0L;
        private Sequence() {}
    }
}
