/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.exception;

import com.viettel.arpu.constant.AppConstants;
import com.viettel.arpu.model.response.ErrorResponse;
import com.viettel.arpu.model.response.ErrorResponseSupport;

/**
 * @author DoDV
 * @Date :6/8/2020, Mon
 */
public class FileInvalidException extends RuntimeException implements ErrorResponseSupport {
    private static final ErrorResponse error = new ErrorResponse(AppConstants.FILE_INVALID);

    public FileInvalidException() {
        super(error.getMessage());
    }

    @Override
    public ErrorResponse toErrorResponse() {
        return error;
    }
}
