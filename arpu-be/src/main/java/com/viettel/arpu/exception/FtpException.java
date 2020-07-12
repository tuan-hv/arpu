/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.exception;

import com.viettel.arpu.model.response.ErrorResponse;
import com.viettel.arpu.model.response.ErrorResponseSupport;

/**
 * Trả ra thông báo lỗi khi xảy ra bất kì 1 ngoại lệ liên quan tới server sftp
 * Ví dụ vấn đề kết nối, vấn đề đọc file.
 */
public class FtpException extends RuntimeException implements ErrorResponseSupport {
    public FtpException(Throwable cause) {
        super(cause);
    }

    @Override
    public ErrorResponse toErrorResponse() {
        return null;
    }
}
