/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.constant.enums;

/**
 * @Author VuHQ
 * @Since 6/8/2020
 */
public enum LoanStatus {
    NOT_BORROWED("KVS_01")
    , LOAN_BORROWING("KVS_02")
    , NOT_FINALIZED("KVS_03")
    , COMPLETED("KVS_04")
    , REFUSE_LOAN("KVS_05");

    private String status;

    public String getStatus() {
        return status;
    }

    LoanStatus(String status) {
        this.status = status;
    }
}
