/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.model.dto.mb;

import com.viettel.arpu.constant.CommonConstant;
import com.viettel.arpu.model.response.mb.MbBaseResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Author VuHQ
 * @Since 5/29/2020
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MbLoanLimitDTO extends MbBaseResponse {

    private String minAmount = CommonConstant.DEFAULT_LIMIT;
    private String maxAmount = CommonConstant.DEFAULT_LIMIT;
    private String currentLimit = CommonConstant.DEFAULT_LIMIT;
    private String loanAmount = CommonConstant.DEFAULT_LIMIT;
}
