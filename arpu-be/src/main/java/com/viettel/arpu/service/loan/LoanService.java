/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.service.loan;

import com.viettel.arpu.model.dto.LoanDTO;
import com.viettel.arpu.model.dto.LoanDetailDTO;
import com.viettel.arpu.model.dto.UpdateLoanDTO;
import com.viettel.arpu.model.request.SearchApproveForm;
import com.viettel.arpu.model.request.SearchLoanForm;
import com.viettel.arpu.model.request.mb.LoanConfirmRequestForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

/**
 * @Author VuHQ
 * @Since 5/25/2020
 */
public interface LoanService {

    Page<LoanDTO> searchApprovalByForm(SearchApproveForm searchApproveForm, Pageable pageable);

    Page<LoanDTO> searchLoanByForm(SearchLoanForm searchLoanForm, Pageable pageable);

    LoanDetailDTO getLoanDetail(Long id);

    UpdateLoanDTO updateToMBApproval(LoanConfirmRequestForm loanConfirmRequestForm) throws IOException;

    void updatePayLoan(List<String> loanAccount) throws Exception;
}
