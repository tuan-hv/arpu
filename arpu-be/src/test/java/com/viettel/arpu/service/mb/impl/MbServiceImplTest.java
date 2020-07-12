package com.viettel.arpu.service.mb.impl;

import com.viettel.arpu.config.FtpStorageProperties;
import com.viettel.arpu.config.MbStorageProperties;
import com.viettel.arpu.controller.mb.MBController;
import com.viettel.arpu.exception.CodeInvalidException;
import com.viettel.arpu.exception.DataInvalidException;
import com.viettel.arpu.exception.LoanHasBeenApprovedException;
import com.viettel.arpu.exception.LoanNotFoundException;
import com.viettel.arpu.exception.LoanStatusInvalidException;
import com.viettel.arpu.exception.MbResponseException;
import com.viettel.arpu.exception.ReasonMissingException;
import com.viettel.arpu.model.entity.Address;
import com.viettel.arpu.model.entity.CodeCode;
import com.viettel.arpu.model.entity.Customer;
import com.viettel.arpu.model.entity.CustomerRef;
import com.viettel.arpu.model.entity.Interest;
import com.viettel.arpu.model.entity.Loan;
import com.viettel.arpu.model.entity.Reference;
import com.viettel.arpu.model.request.LoanApprovalForm;
import com.viettel.arpu.model.response.mb.MBKYCResponse;
import com.viettel.arpu.repository.CodeCodeRepository;
import com.viettel.arpu.repository.CustomerRepository;
import com.viettel.arpu.repository.InterestRepository;
import com.viettel.arpu.repository.LoanRepository;
import com.viettel.arpu.service.mb.MBService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MbServiceImplTest {
    @Autowired
    MBService mbService;

    @Autowired
    MBController mbController;

    @MockBean
    CustomerRepository customerRepository;
    @MockBean
    CodeCodeRepository codeCodeRepository;
    @MockBean
    LoanRepository loanRepository;
    @MockBean
    InterestRepository interestRepository;
    @MockBean
    MbStorageProperties mbStorageProperties;
    @MockBean
    RestTemplate restTemplate;
    @MockBean
    RetryTemplate retryTemplate;
    @MockBean
    FtpStorageProperties ftpStorageProperties;


    @Test
    @DisplayName("Update Loan with id invalid")
    public void testUploadLoanWithIdInvalid() {

        LoanApprovalForm loanApprovalForm = new LoanApprovalForm();
        loanApprovalForm.setLoanId("abc");

        Exception exception = Assertions.assertThrows(NumberFormatException.class, () ->
                mbService.updateLoanInfo(loanApprovalForm));
        String expectedMessage = "For input string: \"abc\"";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(actualMessage, expectedMessage);

    }

    @Test
    @DisplayName("Update Loan with id not found")
    public void testUploadLoanWithIdNotFound() {
        Loan loan = initData();
        Mockito.when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        LoanApprovalForm loanApprovalForm = new LoanApprovalForm();
        loanApprovalForm.setLoanId("2");
        Exception exception = Assertions.assertThrows(LoanNotFoundException.class, () ->
                mbController.updateLoanInfo(loanApprovalForm));
        String expectedMessage = "can not get loan by id.";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(actualMessage, expectedMessage);

    }

    @Test
    @DisplayName("Update Loan with data not exist")
    public void testUploadLoanWithDataNotExist() {
        Loan loan = initData();
        Mockito.when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        CodeCode codeCode = new CodeCode();
        codeCode.setId("PDS_02");
        loan.setApprovalStatus(codeCode);
        LoanApprovalForm loanApprovalForm = new LoanApprovalForm();
        loanApprovalForm.setLoanId("1");
        loanApprovalForm.setSourceNumber("0997487545");
        loanApprovalForm.setIdentityCardNumber("0997487545");
        loanApprovalForm.setSourceMobile("0997487545");
        loanApprovalForm.setIdentityCardType("CMND");
        loanApprovalForm.setCodeId("PDS_03");
        Exception exception = Assertions.assertThrows(DataInvalidException.class, () ->
                mbController.updateLoanInfo(loanApprovalForm));

        String expectedMessage = "Data input not match with database";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(actualMessage, expectedMessage);

    }

    @Test
    @DisplayName("Update Loan with code invalid")
    public void testUploadLoanWithCodeInvalid() {
        CodeCode codeCode = new CodeCode();
        codeCode.setId("test");
        Loan loan = initData();
        loan.setApprovalStatus(codeCode);
        Mockito.when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        LoanApprovalForm loanApprovalForm = new LoanApprovalForm();
        loanApprovalForm.setLoanId("1");
        Exception exception = Assertions.assertThrows(CodeInvalidException.class, () ->
                mbController.updateLoanInfo(loanApprovalForm));

        String expectedMessage = "Code input invalid";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(actualMessage, expectedMessage);

    }

    @Test
    @DisplayName("Update Loan with status PDS_03")
    public void testUploadLoanWithLoanInvalidPDS_03() {
        CodeCode codeCode = new CodeCode();
        codeCode.setId("PDS_03");
        Loan loan = initData();
        loan.setApprovalStatus(codeCode);
        Mockito.when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        LoanApprovalForm loanApprovalForm = new LoanApprovalForm();
        loanApprovalForm.setLoanId("1");

        Exception exception = Assertions.assertThrows(LoanHasBeenApprovedException.class, () ->
                mbController.updateLoanInfo(loanApprovalForm));

        String expectedMessage = "Loan has been approved or rejected";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(actualMessage, expectedMessage);


    }

    @Test
    @DisplayName("Update Loan with status PDS_04")
    public void testUploadLoanWithLoanInvalidPDS_04() {
        CodeCode codeCode = new CodeCode();
        codeCode.setId("PDS_04");
        Loan loan = initData();
        loan.setApprovalStatus(codeCode);
        Mockito.when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        LoanApprovalForm loanApprovalForm = new LoanApprovalForm();
        loanApprovalForm.setLoanId("1");

        Exception exception = Assertions.assertThrows(LoanHasBeenApprovedException.class, () ->
                mbController.updateLoanInfo(loanApprovalForm));

        String expectedMessage = "Loan has been approved or rejected";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(actualMessage, expectedMessage);

    }

    @Test
    @DisplayName("Update Loan with status PDS_01")
    public void testUploadLoanWithLoanInvalidPDS_01() {
        CodeCode codeCode = new CodeCode();
        codeCode.setId("PDS_01");
        Loan loan = initData();
        loan.setApprovalStatus(codeCode);
        Mockito.when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        LoanApprovalForm loanApprovalForm = new LoanApprovalForm();
        loanApprovalForm.setLoanId("1");

        Exception exception = Assertions.assertThrows(LoanHasBeenApprovedException.class, () ->
                mbController.updateLoanInfo(loanApprovalForm));

        String expectedMessage = "Loan has been approved or rejected";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(actualMessage, expectedMessage);

    }

    @Test
    @DisplayName("Reject Loan with no resion PDS_04")
    public void testRejectLoanWithWithNoResionPDS_04() {
        CodeCode codeCode = new CodeCode();
        codeCode.setId("PDS_02");
        Loan loan = initData();
        loan.setApprovalStatus(codeCode);
        Mockito.when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        LoanApprovalForm loanApprovalForm = new LoanApprovalForm();
        loanApprovalForm.setLoanId("1");
        loanApprovalForm.setCodeId("PDS_04");

        Exception exception = Assertions.assertThrows(ReasonMissingException.class, () ->
                mbController.updateLoanInfo(loanApprovalForm));
        String expectedMessage = "Reason can not be null when reject loan.";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(actualMessage, expectedMessage);

    }

    @Test
    @DisplayName("Reject Loan with no resion PDS_01")
    public void testRejectLoanWithWithNoResionPDS_01() {
        CodeCode codeCode = new CodeCode();
        codeCode.setId("PDS_02");
        Loan loan = initData();
        loan.setApprovalStatus(codeCode);
        Mockito.when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        LoanApprovalForm loanApprovalForm = new LoanApprovalForm();
        loanApprovalForm.setLoanId("1");
        loanApprovalForm.setCodeId("PDS_01");

        Exception exception = Assertions.assertThrows(ReasonMissingException.class, () ->
                mbController.updateLoanInfo(loanApprovalForm));
        String expectedMessage = "Reason can not be null when reject loan.";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(actualMessage, expectedMessage);

    }

    @Test
    void testReturnExceptionWhenCallAPIMBReturnFalse() {
        Mockito.when(mbStorageProperties.getKyc()).thenReturn("abcd");

        Loan loan = initData();

        Mockito.when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        MBKYCResponse mbBaseResponse = new MBKYCResponse();
        mbBaseResponse.setErrorDesc("11");
        mbBaseResponse.setErrorCode("11");

        ResponseEntity responseEntity = new ResponseEntity(mbBaseResponse, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any())
        ).thenReturn(responseEntity);

        LoanApprovalForm loanApprovalForm = new LoanApprovalForm();
        loanApprovalForm.setLoanId("1");
        loanApprovalForm.setCodeId("PDS_03");
        loanApprovalForm.setSourceMobile("0395954111");
        loanApprovalForm.setSourceNumber("3");
        loanApprovalForm.setReason("Ly do tu choi");
        loanApprovalForm.setIdentityCardNumber("24354545");
        loanApprovalForm.setIdentityCardType("CMTND");
        loanApprovalForm.setRequestId("aaaa");


        Exception exception = Assertions.assertThrows(MbResponseException.class, () ->
                mbService.updateLoanInfo(loanApprovalForm));

        String expectedMessage = "11";
        String expectedCode = "11";
        String actualMessage = exception.getMessage();
        String actualCode = exception.getMessage();
        Assertions.assertEquals(actualMessage, expectedMessage);
        Assertions.assertEquals(expectedCode, actualCode);
    }

    @Test
    void testShouldSendToMBApprove() {
        Mockito.when(mbStorageProperties.getKyc()).thenReturn("abcd");

        Loan loan = initData();

        CodeCode codeCode = new CodeCode();
        codeCode.setId("PDS_02");
        codeCode.setCodeName("test");
        codeCode.setCodeType("test");
        codeCode.setCodeTypeName("aa");

        CodeCode codeCode1 = new CodeCode();
        codeCode1.setId("KVS_02");
        codeCode1.setCodeName("test");
        codeCode.setCodeType("test");
        codeCode1.setCodeTypeName("aa");

        loan.setApprovalStatus(codeCode);
        Mockito.when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        Mockito.when(codeCodeRepository.findById("PDS_03")).thenReturn(Optional.of(codeCode));

        Mockito.when(codeCodeRepository.findById("KVS_02")).thenReturn(Optional.of(codeCode));

        MBKYCResponse mbBaseResponse = new MBKYCResponse();
        mbBaseResponse.setErrorDesc("00");
        mbBaseResponse.setErrorCode("00");

        ResponseEntity responseEntity = new ResponseEntity(mbBaseResponse, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any())
        ).thenReturn(responseEntity);

        LoanApprovalForm loanApprovalForm = new LoanApprovalForm();
        loanApprovalForm.setLoanId("1");
        loanApprovalForm.setCodeId("PDS_03");
        loanApprovalForm.setSourceMobile("0395954111");
        loanApprovalForm.setSourceNumber("3");
        loanApprovalForm.setReason("Ly do tu choi");
        loanApprovalForm.setIdentityCardNumber("24354545");
        loanApprovalForm.setIdentityCardType("CMTND");
        loanApprovalForm.setRequestId("aaaa");

        MBKYCResponse respone = mbService.updateLoanInfo(loanApprovalForm);

        String expectedErrorDesc = "00";
        String expectedErrorCode = "00";
        String expectedData = null;

        Assertions.assertEquals(expectedErrorDesc, respone.getErrorDesc());
        Assertions.assertEquals(expectedErrorCode, respone.getErrorCode());
        Assertions.assertEquals(expectedData, respone.getErrorData());

    }

    @Test
    void testShouldReturnExceptionWhenLoanHasbeenApproval() {
        Mockito.when(mbStorageProperties.getKyc()).thenReturn("abcd");
        Loan loan = initData();

        CodeCode codeCode = new CodeCode();
        codeCode.setId("PDS_03");
        codeCode.setCodeName("test");
        codeCode.setCodeType("test");
        codeCode.setCodeTypeName("aa");

        CodeCode codeCode1 = new CodeCode();
        codeCode1.setId("KVS_02");
        codeCode1.setCodeName("test");
        codeCode.setCodeType("test");
        codeCode1.setCodeTypeName("aa");

        loan.setApprovalStatus(codeCode);
        Mockito.when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        Mockito.when(codeCodeRepository.findById("PDS_03")).thenReturn(Optional.of(codeCode));

        Mockito.when(codeCodeRepository.findById("KVS_02")).thenReturn(Optional.of(codeCode));

        MBKYCResponse mbBaseResponse = new MBKYCResponse();
        mbBaseResponse.setErrorDesc("00");
        mbBaseResponse.setErrorCode("00");

        ResponseEntity responseEntity = new ResponseEntity(mbBaseResponse, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any())
        ).thenReturn(responseEntity);

        LoanApprovalForm loanApprovalForm = new LoanApprovalForm();
        loanApprovalForm.setLoanId("1");
        loanApprovalForm.setCodeId("PDS_03");
        loanApprovalForm.setSourceMobile("0395954111");
        loanApprovalForm.setSourceNumber("3");
        loanApprovalForm.setReason("Ly do tu choi");
        loanApprovalForm.setIdentityCardNumber("24354545");
        loanApprovalForm.setIdentityCardType("CMTND");
        loanApprovalForm.setRequestId("aaaa");

        Exception exception = Assertions.assertThrows(LoanHasBeenApprovedException.class, () ->
                mbService.updateLoanInfo(loanApprovalForm));

        String expectedMessage = "Loan has been approved or rejected";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(actualMessage, expectedMessage);

    }

    @Test
    void testShouldReturnExceptionWhenLoanStatusInvalidPDS03() {
        Mockito.when(mbStorageProperties.getKyc()).thenReturn("abcd");
        Loan loan = initData();

        CodeCode codeCode = new CodeCode();
        codeCode.setId("PDS_05");
        codeCode.setCodeName("test");
        codeCode.setCodeType("test");
        codeCode.setCodeTypeName("aa");

        CodeCode codeCode1 = new CodeCode();
        codeCode1.setId("KVS_02");
        codeCode1.setCodeName("test");
        codeCode.setCodeType("test");
        codeCode1.setCodeTypeName("aa");

        loan.setApprovalStatus(codeCode);

        Mockito.when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        Mockito.when(codeCodeRepository.findById("PDS_03")).thenReturn(Optional.of(codeCode));

        Mockito.when(codeCodeRepository.findById("KVS_02")).thenReturn(Optional.of(codeCode));

        MBKYCResponse mbBaseResponse = new MBKYCResponse();
        mbBaseResponse.setErrorDesc("00");
        mbBaseResponse.setErrorCode("00");

        ResponseEntity responseEntity = new ResponseEntity(mbBaseResponse, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any())
        ).thenReturn(responseEntity);

        LoanApprovalForm loanApprovalForm = new LoanApprovalForm();
        loanApprovalForm.setLoanId("1");
        loanApprovalForm.setCodeId("PDS_03");
        loanApprovalForm.setSourceMobile("0395954111");
        loanApprovalForm.setSourceNumber("3");
        loanApprovalForm.setReason("Ly do tu choi");
        loanApprovalForm.setIdentityCardNumber("24354545");
        loanApprovalForm.setIdentityCardType("CMTND");
        loanApprovalForm.setRequestId("aaaa");

        Exception exception = Assertions.assertThrows(LoanStatusInvalidException.class, () ->
                mbService.updateLoanInfo(loanApprovalForm));

        String expectedMessage = "The loan has not been submitted to MB for approval";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(actualMessage, expectedMessage);

    }

    @Test
    void testShouldReturnExceptionWhenLoanStatusInvalidPDS01() {
        Mockito.when(mbStorageProperties.getKyc()).thenReturn("abcd");
        Loan loan = initData();

        CodeCode codeCode = new CodeCode();
        codeCode.setId("PDS_05");
        codeCode.setCodeName("test");
        codeCode.setCodeType("test");
        codeCode.setCodeTypeName("aa");

        CodeCode codeCode1 = new CodeCode();
        codeCode1.setId("KVS_02");
        codeCode1.setCodeName("test");
        codeCode.setCodeType("test");
        codeCode1.setCodeTypeName("aa");

        loan.setApprovalStatus(codeCode);

        Mockito.when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        Mockito.when(codeCodeRepository.findById("PDS_03")).thenReturn(Optional.of(codeCode));

        Mockito.when(codeCodeRepository.findById("KVS_02")).thenReturn(Optional.of(codeCode));

        MBKYCResponse mbBaseResponse = new MBKYCResponse();
        mbBaseResponse.setErrorDesc("00");
        mbBaseResponse.setErrorCode("00");

        ResponseEntity responseEntity = new ResponseEntity(mbBaseResponse, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any())
        ).thenReturn(responseEntity);

        LoanApprovalForm loanApprovalForm = new LoanApprovalForm();
        loanApprovalForm.setLoanId("1");
        loanApprovalForm.setCodeId("PDS_01");
        loanApprovalForm.setSourceMobile("0395954111");
        loanApprovalForm.setSourceNumber("3");
        loanApprovalForm.setReason("Ly do tu choi");
        loanApprovalForm.setIdentityCardNumber("24354545");
        loanApprovalForm.setIdentityCardType("CMTND");
        loanApprovalForm.setRequestId("aaaa");

        Exception exception = Assertions.assertThrows(LoanStatusInvalidException.class, () ->
                mbService.updateLoanInfo(loanApprovalForm));

        String expectedMessage = "The loan has not been submitted to MB for approval";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(actualMessage, expectedMessage);

    }

    @Test
    void testShouldReturnExceptionWhenLoanStatusInvalidPDS04() {
        Mockito.when(mbStorageProperties.getKyc()).thenReturn("abcd");
        Loan loan = initData();

        CodeCode codeCode = new CodeCode();
        codeCode.setId("PDS_05");
        codeCode.setCodeName("test");
        codeCode.setCodeType("test");
        codeCode.setCodeTypeName("aa");

        CodeCode codeCode1 = new CodeCode();
        codeCode1.setId("KVS_02");
        codeCode1.setCodeName("test");
        codeCode.setCodeType("test");
        codeCode1.setCodeTypeName("aa");

        loan.setApprovalStatus(codeCode);

        Mockito.when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        Mockito.when(codeCodeRepository.findById("PDS_03")).thenReturn(Optional.of(codeCode));

        Mockito.when(codeCodeRepository.findById("KVS_02")).thenReturn(Optional.of(codeCode));

        MBKYCResponse mbBaseResponse = new MBKYCResponse();
        mbBaseResponse.setErrorDesc("00");
        mbBaseResponse.setErrorCode("00");

        ResponseEntity responseEntity = new ResponseEntity(mbBaseResponse, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any())
        ).thenReturn(responseEntity);

        LoanApprovalForm loanApprovalForm = new LoanApprovalForm();
        loanApprovalForm.setLoanId("1");
        loanApprovalForm.setCodeId("PDS_04");
        loanApprovalForm.setSourceMobile("0395954111");
        loanApprovalForm.setSourceNumber("3");
        loanApprovalForm.setReason("Ly do tu choi");
        loanApprovalForm.setIdentityCardNumber("24354545");
        loanApprovalForm.setIdentityCardType("CMTND");
        loanApprovalForm.setRequestId("aaaa");

        Exception exception = Assertions.assertThrows(LoanStatusInvalidException.class, () ->
                mbService.updateLoanInfo(loanApprovalForm));

        String expectedMessage = "The loan has not been submitted to MB for approval";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(actualMessage, expectedMessage);

    }

    public Loan initData() {
        Loan loan = new Loan();
        CodeCode codeCode = new CodeCode();
        Customer customer = new Customer();
        Address address = new Address();
        Set<CustomerRef> customerRefSet = new HashSet<>();
        Reference reference = new Reference();
        Interest interest = new Interest();
        CustomerRef customerRef = new CustomerRef();

        // init data code
        codeCode.setId("MQHS_01");
        codeCode.setCodeName("Bố");
        codeCode.setCodeType("4");
        codeCode.setCodeTypeName("Mối quan hệ");

        // init data address
        address.setAddressDetail("To 1");
        address.setVillage("La Khe");
        address.setDistrict("Ha Dong");
        address.setProvince("Ha Noi");

        // init data customerRef
        customerRef.setRelationship(codeCode);
        customerRef.setCustomer(customer);
        customerRef.setRelationship(codeCode);
        customerRef.setReference(reference);

        customerRefSet.add(customerRef);
        loan.setCustomerRef(customerRef);

        // init data customer
        customer.setId(1L);
        customer.setCustomerAccount("test");
        customer.setAddress(address);
        customer.setCustomerRef(customerRefSet);

        customer.setIdentityType("CMTND");
        customer.setViettelpayWallet("3");
        customer.setMsisdn("0395954111");
        customer.setIdentityNumber("24354545");
        customer.setIdentityType("CMTND");

        // init data loan
        loan.setId(1L);
        loan.setCustomer(customer);
        loan.setLoanStatus(codeCode);
        loan.setApprovalStatus(codeCode);
        loan.setInterest(interest);
        loan.setArpuLatestThreeMonths(new BigDecimal("10000"));
        loan.setFee(new BigDecimal("10000"));
        loan.setContractLink("google.com");
        loan.setLoanAccount("DoDV");
        loan.setAmountSpent(new BigDecimal("10000"));
        loan.setLimitRemaining(new BigDecimal("10000"));
        loan.setProfitAmount(new BigDecimal("10000"));
        loan.setAmountPay(new BigDecimal("10000"));

        return loan;
    }
}
