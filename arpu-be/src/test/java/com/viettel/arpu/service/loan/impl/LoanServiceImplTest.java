/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */

package com.viettel.arpu.service.loan.impl;

import com.viettel.arpu.MainApplication;
import com.viettel.arpu.constant.enums.Gender;
import com.viettel.arpu.model.dto.LoanDTO;
import com.viettel.arpu.model.entity.Customer;
import com.viettel.arpu.model.entity.Loan;
import com.viettel.arpu.model.request.SearchApproveForm;
import com.viettel.arpu.model.request.SearchLoanForm;
import com.viettel.arpu.repository.LoanRepository;
import com.viettel.arpu.service.loan.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @Author User
 * @Since 5/22/2020
 */
@SpringBootTest(classes = MainApplication.class)
class LoanServiceImplTest {


    @InjectMocks
    private LoanService loanService = new LoanServiceImpl();

    @Mock
    LoanRepository loanRepository;

    private Pageable pageable;
    private Page page;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        Loan loan = new Loan();
        Customer customer = new Customer();
        customer.setFullName("abc");
        customer.setGender(Gender.FEMALE);
        loan.setCustomer(customer);
    }

    @Nested
    class LoanList{
        private Loan firstLoan;
        private Loan secondLoan;
        private List loans;


        @BeforeEach
        void setUp() {
            MockitoAnnotations.initMocks(this);
            firstLoan = new Loan();
            secondLoan = new Loan();
            Customer customer = new Customer();
            customer.setFullName("abc");
            customer.setGender(Gender.FEMALE);
            firstLoan.setCustomer(customer);
            secondLoan.setCustomer(customer);
            loans = Arrays.asList(firstLoan, secondLoan);
        }



        @Test
        @DisplayName("searchApprovalByForm when input true data then return data")
        public void testSearchApproveFormReturnData() {
            pageable = PageRequest.of(0,2);
            page = new PageImpl(loans, pageable, loans.size());
            SearchApproveForm searchApproveForm = new SearchApproveForm();
            searchApproveForm.setApproveStatus("Dang vay");
            searchApproveForm.setFromDate(LocalDate.now());
            searchApproveForm.setToDate(LocalDate.now());
            searchApproveForm.setPhoneNumber("0392558754");
            Mockito.when(loanRepository.findAll(Mockito.any(Specification.class),Mockito.eq(pageable))).thenReturn(page);
            Page<LoanDTO> results = loanService.searchApprovalByForm(searchApproveForm, pageable);
            assertEquals(2, results.getContent().size(), "should return 2 element");
        }

        @Test
        @DisplayName("when search form have data then return page")
        void testSearchLoanWithFormHaveDataReturnData() {
            pageable = PageRequest.of(0,3);
            page = new PageImpl(loans, pageable, loans.size());
            SearchLoanForm searchLoanForm = new SearchLoanForm();
            searchLoanForm.setApproveStatus("PDS_02");
            searchLoanForm.setFromDate(LocalDate.now());
            searchLoanForm.setToDate(LocalDate.now());
            searchLoanForm.setPhoneNumber("0392558754");
            searchLoanForm.setLoanStatus("KVS_02");
            Mockito.when(loanRepository.findAll(Mockito.any(Specification.class),Mockito.eq(pageable))).thenReturn(page);
            Page<LoanDTO> results = loanService.searchLoanByForm(searchLoanForm, pageable);
            assertEquals(2, results.getContent().size(), "should return page have data");
        }
    }

}

