/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.model.request;

import com.viettel.arpu.model.request.mb.MbRequestIdForm;
import com.viettel.arpu.validator.Numeric;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * Response khi gửi duyệt tới API của MB
 */
@Getter
@Setter
public class LoanApprovalForm extends MbRequestIdForm {
    @Numeric
    @NotNull
    private String loanId = "";
    @NotBlank
    private String codeId = "";
    private String reason = "";

    @Numeric
    @Size(max = 12)
    @NotBlank
    private String sourceMobile = "";
    @Numeric
    @NotBlank
    private String sourceNumber = "";

    @NotBlank
    private String identityCardType = "";

    @Numeric
    @NotBlank
    private String identityCardNumber = "";
}
