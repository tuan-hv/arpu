/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.model.request.mb;

import com.viettel.arpu.validator.Phone;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;

/**
 * @Author VuHQ
 * @Since 6/22/2020
 */
@Getter
@Setter
public class MbGetLimitForm extends MbRequestIdForm {
    @Phone
    @NotBlank
    private String sourceMobile = "";
    @NotBlank
    private String sourceNumber ="";
    @NotBlank
    @Range(min = 1, max = 2)
    private String loanType = "";
    private String loanAccount;
}
