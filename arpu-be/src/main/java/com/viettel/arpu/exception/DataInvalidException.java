/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.exception;

import com.viettel.arpu.model.response.ErrorResponse;
import com.viettel.arpu.model.response.ErrorResponseSupport;

import static com.viettel.arpu.constant.ErrorConstant.DATA_INPUT_INVALID;

/**
 * @author DoDV
 * @Date :6/18/2020, Thu
 */
public class DataInvalidException extends RuntimeException implements ErrorResponseSupport {
    public DataInvalidException() {
        super(DATA_INPUT_INVALID.getMessage());
    }

    @Override
    public ErrorResponse toErrorResponse() {
        return DATA_INPUT_INVALID;
    }
}
