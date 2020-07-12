/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.model.request.mb;

import com.viettel.arpu.validator.Otp;
import com.viettel.arpu.validator.Phone;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * @Author VuHQ
 * @Since 6/23/2020
 */
@Getter
@Setter
@ToString
public class MbPinForm extends MbRequestIdForm {
    @NotBlank
    private String product = "";
    private String serviceName = "";
    private String otherProp = "";
    @NotBlank
    @Otp
    private String pin = "";
    @Phone
    @NotBlank
    private String msisdn = "";

    private String appToken = "";
}
