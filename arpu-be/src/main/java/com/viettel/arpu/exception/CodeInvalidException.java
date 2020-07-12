/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.exception;

import com.viettel.arpu.model.response.ErrorResponse;
import com.viettel.arpu.model.response.ErrorResponseSupport;

import static com.viettel.arpu.constant.ErrorConstant.CODE_INPUT_INVALID;

/**
 *
 * @author DoDV
 * @Date :6/11/2020, Thu
 * Khi update trạng thái 1 khoản vay mà truyền vào 1 mã code không hợp lệ
 * response status HttpStatus.BAD_REQUEST (400).
 */

public class CodeInvalidException extends RuntimeException implements ErrorResponseSupport {
    public CodeInvalidException() {
        super(CODE_INPUT_INVALID.getMessage());
    }

    @Override
    public ErrorResponse toErrorResponse() {
        return CODE_INPUT_INVALID;
    }
}
