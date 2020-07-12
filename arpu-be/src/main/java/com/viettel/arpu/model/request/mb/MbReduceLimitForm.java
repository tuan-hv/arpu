/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.model.request.mb;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author VuHQ
 * @Since 6/5/2020
 */
@Getter
@Setter
@ToString
public class MbReduceLimitForm {
    private String reduceAmount = "";
    private String reduceRequestId = "";
    private String sourceMobile = "";
    private String sourceNumber = "";
    private String identityCardType = "";
    private String identityCardNumber = "";
    private String loanAccount = "";
    private String otp = "";
}
