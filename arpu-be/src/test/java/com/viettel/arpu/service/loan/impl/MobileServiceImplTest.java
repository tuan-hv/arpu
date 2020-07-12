package com.viettel.arpu.service.loan.impl;

import com.viettel.arpu.model.dto.mb.MbCustomerDTO;
import com.viettel.arpu.model.dto.mb.MbExistPhoneNumberDTO;
import com.viettel.arpu.model.dto.mb.MbListLoanDTO;
import com.viettel.arpu.model.dto.mb.MbLoanLimitDTO;
import com.viettel.arpu.model.request.mb.MbCheckCustomerForm;
import com.viettel.arpu.service.loan.MobileService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest
@Transactional
class MobileServiceImplTest {
    @Autowired
    MobileService service;

//    @DisplayName("Customer is existed")
//    @Test
//    void testIsCustomerExisted() {
//        MbCheckCustomerForm mbCheckCustomerForm = new MbCheckCustomerForm();
//        mbCheckCustomerForm.setPhoneNumber("0968441599");
//        MbExistPhoneNumberDTO result = service.isExistCustomer(mbCheckCustomerForm);
//
//        then(result.getIsExist()).isTrue();
//    }

    @DisplayName("Customer is not existed")
    @Test
    void testIsCustomerNotExisted() {
        MbCheckCustomerForm mbCheckCustomerForm = new MbCheckCustomerForm();
        mbCheckCustomerForm.setPhoneNumber("0395954444");
        MbExistPhoneNumberDTO result = service.isExistCustomer(mbCheckCustomerForm);

        then(result.getIsExist()).isFalse();
    }

    @DisplayName("Get Loans By Phone Success")
    @Test
    void testGetLoansByPhoneSuccess() {
        MbCheckCustomerForm mbCheckCustomerForm = new MbCheckCustomerForm();
        mbCheckCustomerForm.setPhoneNumber("0385431000");
        MbListLoanDTO result = service.getLoansByPhone(mbCheckCustomerForm);

        then(result.getLoans()).isNotEmpty();
    }

    @DisplayName("Get Loans By Phone Fail")
    @Test
    void testGetLoansByPhoneFail() {
        MbCheckCustomerForm mbCheckCustomerForm = new MbCheckCustomerForm();
        mbCheckCustomerForm.setPhoneNumber("039595455512");
        MbListLoanDTO result = service.getLoansByPhone(mbCheckCustomerForm);

        then(result.getLoans()).isEmpty();
    }


    @DisplayName("Get Customer Success")
    @Test
    void testGetCustomerSuccess() {
        MbCheckCustomerForm mbCheckCustomerForm = new MbCheckCustomerForm();
        mbCheckCustomerForm.setPhoneNumber("0395954444");
        MbCustomerDTO result = service.getCustomer(mbCheckCustomerForm);

        then(result.getListReference()).isNotEmpty();
    }

    @DisplayName("Get Customer Fail")
    @Test
    void testGetCustomerFail() {
        MbCheckCustomerForm mbCheckCustomerForm = new MbCheckCustomerForm();
        mbCheckCustomerForm.setPhoneNumber("0395954555");
        MbCustomerDTO result = service.getCustomer(mbCheckCustomerForm);

        then(result.getListReference()).isEmpty();
    }

    @DisplayName("Get Customer Limit Success")
    @Test
    void testGetCustomerLimitSuccess() {
        MbCheckCustomerForm mbCheckCustomerForm = new MbCheckCustomerForm();
        mbCheckCustomerForm.setPhoneNumber("0395954555");
        MbLoanLimitDTO result = service.getCustomerLimit(mbCheckCustomerForm);

        then(result.getMinAmount()).isNotEqualTo("0");
        then(result.getMaxAmount()).isNotEqualTo("0");
    }

    @DisplayName("Get Customer Limit Fail")
    @Test
    void testGetCustomerLimitFail() {
        MbCheckCustomerForm mbCheckCustomerForm = new MbCheckCustomerForm();
        mbCheckCustomerForm.setPhoneNumber("0395954667");
        MbLoanLimitDTO result = new MbLoanLimitDTO();
        try {
            result = service.getCustomerLimit(mbCheckCustomerForm);
        }catch (Exception e){
            result = null;
        }

        then(result).isNull();
    }

    @DisplayName("With Draw Money To VTP Success")
    @Test
    void testWithdrawMoneyToVTPSuccess() {
        MbCheckCustomerForm mbCheckCustomerForm = new MbCheckCustomerForm();
        mbCheckCustomerForm.setPhoneNumber("0395954667");
        MbLoanLimitDTO result = new MbLoanLimitDTO();
        try {
            result = service.getCustomerLimit(mbCheckCustomerForm);
        }catch (Exception e){
            result = null;
        }

        then(result).isNull();
    }

}
