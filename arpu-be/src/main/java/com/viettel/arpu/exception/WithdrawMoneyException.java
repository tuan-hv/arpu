/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.exception;

import com.viettel.arpu.model.response.ErrorResponse;
import com.viettel.arpu.model.response.ErrorResponseSupport;

import static com.viettel.arpu.constant.MBConstant.WITHDRAW_FAIL;

/**
 * @author TungNV
 * @Date :6/23/2020
 */
public class WithdrawMoneyException extends RuntimeException implements ErrorResponseSupport {

    public WithdrawMoneyException() {
        super(WITHDRAW_FAIL.getMessage());
    }

    @Override
    public ErrorResponse toErrorResponse() {
        return WITHDRAW_FAIL;
    }
}
