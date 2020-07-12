/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.model.response;

@FunctionalInterface
public interface ErrorResponseSupport {
    ErrorResponse toErrorResponse();
}
