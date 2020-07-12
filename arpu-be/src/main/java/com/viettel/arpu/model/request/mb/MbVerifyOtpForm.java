/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.model.request.mb;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * @Author VuHQ
 * @Since 6/26/2020
 */
@Setter
@Getter
@ToString
public class MbVerifyOtpForm extends MbRequestIdForm {
    @NotBlank
    private String signedRequest = "";
    @NotBlank
    private String otp = "";
}
