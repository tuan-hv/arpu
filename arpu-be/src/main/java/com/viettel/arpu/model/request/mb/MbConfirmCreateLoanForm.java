/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.model.request.mb;

import com.viettel.arpu.validator.Otp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * @Author VuHQ
 * @Since 6/17/2020
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MbConfirmCreateLoanForm extends MbRequestIdForm {
    private String sourceMobile;
    private String sourceNumber;
    private String loanRequestId;
    @NotBlank
    @Otp
    private String otp;
}
