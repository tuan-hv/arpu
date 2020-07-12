/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.model.request.mb;

import com.viettel.arpu.validator.Otp;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * @Author VuHQ
 * @Since 6/18/2020
 */
@Getter
@Setter
@ToString
public class MbConfirmLimitForm extends MbRequestIdForm {
    @NotBlank
    @Otp
    private String otp = "";
}
