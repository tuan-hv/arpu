/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.model.response.mb;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @Author VuHQ
 * @Since 6/17/2020
 */
@Getter
@Setter
@NoArgsConstructor
public class MbConfirmCreateLoanResponse extends MbBaseResponse {
    private Boolean isBefore;
    private String loanAccount;
    private String customerAccount;
    private String term;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String interestRate;

}
