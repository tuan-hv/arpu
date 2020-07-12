/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.controller.mb;

import com.viettel.arpu.model.request.LoanApprovalForm;
import com.viettel.arpu.model.response.BaseResponse;
import com.viettel.arpu.model.response.mb.MBKYCResponse;
import com.viettel.arpu.service.mb.MBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/mb/loans")
public class MBController {
    @Autowired
    private MBService mbService;

    @PutMapping("/loan_approval")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<MBKYCResponse> updateLoanInfo(@Valid LoanApprovalForm loanApprovalForm) {
        return new BaseResponse<>(mbService.updateLoanInfo(loanApprovalForm));
    }
}
