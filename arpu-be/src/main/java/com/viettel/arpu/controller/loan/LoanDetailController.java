/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.controller.loan;

import com.viettel.arpu.model.dto.LoanDetailDTO;
import com.viettel.arpu.model.dto.UpdateLoanDTO;
import com.viettel.arpu.model.request.mb.LoanConfirmRequestForm;
import com.viettel.arpu.model.response.BaseResponse;
import com.viettel.arpu.service.loan.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping(value = "api/loans")
public class LoanDetailController {

    @Autowired
    private LoanService loanService;

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<LoanDetailDTO>> getDetail(@PathVariable Long id) {
        return ResponseEntity.ok(new BaseResponse<>(loanService.getLoanDetail(id)));
    }

    @PostMapping("/update_mb_approval")
    public ResponseEntity<BaseResponse<UpdateLoanDTO>> updateToMBApproval(
            @Valid LoanConfirmRequestForm loanConfirmRequestForm) throws IOException {
        return ResponseEntity.ok(new BaseResponse<>(loanService.updateToMBApproval(loanConfirmRequestForm)));
    }
}
