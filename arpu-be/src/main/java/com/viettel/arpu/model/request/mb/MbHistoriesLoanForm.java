/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.model.request.mb;

import com.viettel.arpu.validator.Otp;
import com.viettel.arpu.validator.Phone;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * @Author VuHQ
 * @Since 5/29/2020
 */
@Getter
@Setter
@ToString
public class MbHistoriesLoanForm extends MbRequestIdForm {
    @Phone
    @NotBlank
    private String sourceMobile = "";
    @NotBlank
    private String loanAccount = "";
    @Otp
    @NotBlank
    private String pin = "";
}
