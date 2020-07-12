/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.model.request;

import com.viettel.arpu.validator.FieldMatch;
import com.viettel.arpu.validator.Phone;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

/**
 * @Author VuHQ
 * @Since 5/21/2020
 */
@Getter
@Setter
@FieldMatch(fromDate = "fromDate", toDate = "toDate")
public class SearchLoanForm {
    private LocalDate fromDate = null;
    private LocalDate toDate = null;
    @Phone
    private String phoneNumber = "";
    private String identityNumber = "";
    @NotBlank
    private String approveStatus = "";
    @NotBlank
    private String loanStatus = "";
}
