/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */

package com.viettel.arpu.controller.loan;

import com.viettel.arpu.model.dto.LoanDTO;
import com.viettel.arpu.model.request.SearchLoanForm;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author User
 * @Since 5/22/2020
 */
class LoanControllerTest {

    @InjectMocks
    private LoanController loanController;

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
    @DisplayName("when call api search all loan then return data")
    void testSearchAllLoanReturnData(){
        SearchLoanForm searchLoanForm = new SearchLoanForm();
        searchLoanForm.setApproveStatus("PDS_02");
        searchLoanForm.setFromDate(LocalDate.now());
        searchLoanForm.setToDate(LocalDate.now());
        searchLoanForm.setPhoneNumber("0356447841");
        searchLoanForm.setLoanStatus("KVS_02");

        List<LoanDTO> loanDTOS = getLoanDTOSForSearchAllLoanReturnData();
        String expectedData = loanDTOS.toString();
        Pageable pageableLoan = PageRequest.of(0 , 1);
        Page<LoanDTO> pageLoan = new PageImpl<LoanDTO>(loanDTOS,pageableLoan,loanDTOS.size());

        Mockito.when(loanService.searchLoanByForm(searchLoanForm, pageableLoan))
                .thenReturn(pageLoan);

        String actualData = loanController.searchLoan(searchLoanForm, pageableLoan)
                .getBody().getData().getContent().toString();

        Assertions.assertEquals(expectedData, actualData);
    }

    private List<LoanDTO> getLoanDTOSForSearchAllLoanReturnData() {
        List<LoanDTO> loanDTOS = new ArrayList<LoanDTO>();
        LoanDTO loanDTO = new LoanDTO();
        loanDTO.setApproveStatus("PDS_02");
        loanDTO.setGender("1");
        loanDTO.setId("1");
        loanDTO.setInterestRate("interestRate");
        loanDTO.setLoanAmount("10000");
        loanDTO.setLoanStatus("KVS_02");
        loanDTO.setMaximumLimit("500000");
        loanDTO.setMinimumLimit("3000");
        loanDTO.setAmountSpent("1000");
        loanDTO.setLoanTerm("loanTerm");
        loanDTO.setContractLink("contractLink");
        loanDTO.setCreatedDate(LocalDateTime.now());
        loanDTO.setCurrentDebtBalance("currentDebtBalance");
        loanDTO.setFullName("fullName");
        loanDTO.setDateOfIssue(LocalDate.now());
        loanDTO.setIdentityNumber("123131141");
        loanDTO.setIdentityType("CMTND");
        loanDTO.setLimitRemaining("limitRemaining");
        loanDTO.setMinAmountPaid("13123131");
        loanDTO.setLoanAccount("loanAccount");
        loanDTO.setRepaymentForm("repaymentForm");
        loanDTO.setMsisdn("84346655789");
        loanDTO.setPlaceOfIssue("HA Noi");

        loanDTOS.add(loanDTO);

        return loanDTOS;
    }
}
