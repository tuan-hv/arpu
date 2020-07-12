/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.model.response;

import com.viettel.arpu.constant.AppConstants;
import com.viettel.arpu.constant.MessageCode;
import com.viettel.arpu.locale.Translator;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

/**
 * Format of response returned to client
 *
 * @author trungnb3
 * @version 1.0
 * @since 2020/03/01
 */
@Getter
@Setter
public class BaseResponse<T> extends MessageCode implements IBaseResponse<T> {
    protected T data;

    public BaseResponse() {
        super(AppConstants.SUCCESS.getCode(), AppConstants.SUCCESS.getMessage());
    }

    public BaseResponse(T data) {
        this();
        this.data = data;

        if (data == null) {
            this.code = AppConstants.MSG_30.getCode();
            this.message = AppConstants.MSG_30.getMessage();
        }
    }

    public BaseResponse(MessageCode messageCode, T data) {
        this(data);
        Optional.ofNullable(messageCode).ifPresent((MessageCode rc) -> {
            this.code = rc.getCode();
            this.message = rc.getMessage();
        });
    }

    @Override
    public String getMessage() {
        return Translator.toLocale(message);
    }

    @Override
    public boolean isEmpty() {
        return this.data == null;
    }
}
