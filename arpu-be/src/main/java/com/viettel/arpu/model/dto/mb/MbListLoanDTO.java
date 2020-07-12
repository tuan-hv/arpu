/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.model.dto.mb;

import com.viettel.arpu.constant.CommonConstant;
import com.viettel.arpu.model.dto.LoanDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class MbListLoanDTO {
    private String lastStatus = "";
    private String totalElement = CommonConstant.DEFAULT_LIMIT;
    private List<LoanDTO> loans = new ArrayList<>();
}
