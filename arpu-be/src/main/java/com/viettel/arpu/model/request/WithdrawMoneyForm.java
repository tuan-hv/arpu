/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.model.request;

import com.viettel.arpu.validator.Phone;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @Author TungNV
 * @Since 6/23/2020
 */
@Getter
@Setter
public class WithdrawMoneyForm {
    @NotBlank
    @Phone
    private String sourceMobile = "";
    @NotBlank
    private String amount = "";
    @NotBlank
    private String bankAccount = "";
    @NotBlank
    private String otp = "";
}
