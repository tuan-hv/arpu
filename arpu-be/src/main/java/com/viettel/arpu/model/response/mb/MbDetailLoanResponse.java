/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.model.response.mb;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @Author VuHQ
 * @Since 6/28/2020
 */
@Getter
@Setter
@ToString
public class MbDetailLoanResponse extends MbBaseResponse {
    private String loanAccount;
    private String term;
    private String payType;
    private String loanLimit;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String interestRate;
    private String loanAmount;
    private String interestAmount;
    private String feeOverDue;
    private String prOverDue;
    private String customerAccount;
}
