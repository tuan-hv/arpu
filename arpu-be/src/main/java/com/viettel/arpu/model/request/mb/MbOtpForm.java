/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.model.request.mb;

import com.viettel.arpu.validator.Phone;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * @Author VuHQ
 * @Since 6/25/2020
 */
@Getter
@Setter
@ToString
public class MbOtpForm extends MbRequestIdForm {
    @NotBlank
    private String product = "";
    private String serviceName = "";
    private String otherProp = "";
    @Phone
    @NotBlank
    private String msisdn = "";
    @NotBlank
    private String appToken = "";
}
