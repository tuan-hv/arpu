/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.service.mb;

import com.viettel.arpu.model.request.LoanApprovalForm;
import com.viettel.arpu.model.response.mb.MBKYCResponse;


/**
 * @author DoDV
 * @Date :6/5/2020, Fri
 */
@FunctionalInterface
public interface MBService {
    MBKYCResponse updateLoanInfo(LoanApprovalForm loanApprovalForm);
}
