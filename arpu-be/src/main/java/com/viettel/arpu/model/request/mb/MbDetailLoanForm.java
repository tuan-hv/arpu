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
 * @Since 6/28/2020
 */
@Getter
@Setter
@ToString
public class MbDetailLoanForm extends MbRequestIdForm {
    @NotBlank
    private String sourceMobile;

    @NotBlank
    private String loanAccount;
}
