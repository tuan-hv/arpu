/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.constant;

public class BatchConstant {
    public static final String WHITE_LIST = "WHITE_LIST";
    public static final String PAID_LOAN = "PAID_LOAN";
    public static final String MALE = "NAM";
    public static final String FEMALE = "NU";
    public static final long WHITE_LIST_BATCH_ID = 1l;
    public static final long PAY_LOAN_BATCH_ID = 2l;
    public static final long INIT_VERSION_VALUE = 1l;
    public static final String WHITE_LIST_BATCH_NAME = "WhiteList batch";
    public static final String WHITE_LIST_DATA_IS_UNI = "error.msg.sync.whitelist.uni";
    public static final String PAY_LOAN_BATCH_NAME = "Pay loan batch";
    public static final String SYNC_LOAN_ACCOUNT_NOT_FOUND = "error.msg.sync.payLoan.account.not_found";
    public static final String ERROR_SYNC_WHITE_LIST_LENGTH_FILE = "error.msg.sync.whitelist.length.min";
    public static final String ERROR_SYNC_PAY_LOAN_LENGTH_FILE = "error.msg.sync.payLoan.length.min";
    public static final String SYNC_NON_RECORD_PASS_VALID = "error.msg.sync.whitelist.validate.non_record_pass";
    public static final String SYNC_CONNECT_FAILED = "error.msg.sync.whitelist.connect.failed";
    public static final long WHITE_LIST_INIT_VERSION_VALUE = 1l;

    private BatchConstant() {
    }
}

