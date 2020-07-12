/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */

package com.viettel.arpu.controller.loan;

import com.viettel.arpu.model.dto.LoanDTO;
import com.viettel.arpu.model.request.SearchApproveForm;
import com.viettel.arpu.service.loan.LoanService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author User
 * @Since 5/22/2020
 */
class LoanApprovalControllerTest {

    @InjectMocks
    private LoanApprovalController loanApprovalController;

    @Mock
    private LoanService loanService;

    Pageable pageable;
    LoanDTO loanDTO;
    Page page;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        loanDTO = new LoanDTO();
        List<LoanDTO> listResult = new ArrayList<>();
        pageable = PageRequest.of(0,3);
        page = new PageImpl<>(listResult, pageable, 3);
    }


    @Test
    @DisplayName("when call api search all approval loan then return data")
    void testSearchAllLoanApprovalReturnData(){
        SearchApproveForm searchApproveForm = new SearchApproveForm();
        searchApproveForm.setApproveStatus("Dang vay");
        searchApproveForm.setFromDate(LocalDate.now());
        searchApproveForm.setToDate(LocalDate.now());
        searchApproveForm.setPhoneNumber("0356447841");
        Mockito.when(loanService.searchApprovalByForm(searchApproveForm,pageable))
                .thenReturn(page);
        Assertions.assertEquals(3,
                loanApprovalController.searchLoanApproval(pageable, searchApproveForm)
                        .getBody().getData().getTotalElements(),"should return data");
    }
}
