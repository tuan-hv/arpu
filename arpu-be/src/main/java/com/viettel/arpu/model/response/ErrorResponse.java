/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.model.response;

import com.viettel.arpu.constant.MessageCode;
import com.viettel.arpu.locale.Translator;
import lombok.Getter;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "error")
@Getter
public class ErrorResponse extends MessageCode {

    //errors in API request processing
    private final List<String> details;

    public ErrorResponse(MessageCode code) {
        this(code.getCode(), code.getMessage(), new ArrayList<>());
    }

    public ErrorResponse(MessageCode code, List<String> details) {
        this(code.getCode(), code.getMessage(), details);
    }

    public ErrorResponse(String code, String message) {
        this(code, message, new ArrayList<>());
    }

    public ErrorResponse(String code, String message, List<String> details) {
        super(code, message);
        this.details = details;
    }

    @Override
    public String getMessage() {
        return Translator.toLocale(message);
    }
}
