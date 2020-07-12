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
 * @Since 6/18/2020
 */
@Getter
@Setter
@ToString
public class MbConfirmFinalLoanInMbForm {
    private String otp = "";
    private String sourceMobile = "";
    private String sourceNumber = "";
    private String finalRequestId = "";
}
