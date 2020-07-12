package com.viettel.arpu.service.loan.impl;

import com.viettel.arpu.config.FtpStorageProperties;
import com.viettel.arpu.constant.AppConstants;
import com.viettel.arpu.controller.loan.LoanDetailController;
import com.viettel.arpu.exception.FileInvalidException;
import com.viettel.arpu.exception.FtpException;
import com.viettel.arpu.exception.LoanNotFoundException;
import com.viettel.arpu.locale.Translator;
import com.viettel.arpu.model.dto.LoanDetailDTO;
import com.viettel.arpu.model.dto.UpdateLoanDTO;
import com.viettel.arpu.model.entity.Address;
import com.viettel.arpu.model.entity.CodeCode;
import com.viettel.arpu.model.entity.Customer;
import com.viettel.arpu.model.entity.CustomerRef;
import com.viettel.arpu.model.entity.Interest;
import com.viettel.arpu.model.entity.Loan;
import com.viettel.arpu.model.entity.Reference;
import com.viettel.arpu.model.request.mb.LoanConfirmRequestForm;
import com.viettel.arpu.model.response.BaseResponse;
import com.viettel.arpu.repository.CodeCodeRepository;
import com.viettel.arpu.repository.CustomerRepository;
import com.viettel.arpu.repository.LoanRepository;
import com.viettel.arpu.repository.ReferenceRepository;
import com.viettel.arpu.service.loan.LoanService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;


@RunWith(SpringRunner.class)
@SpringBootTest
public class LoanDetailServiceTest {

    @Autowired
    LoanDetailController loanDetailController;
    @Autowired
    LoanService loanService;
    @MockBean
    CustomerRepository customerRepository;
    @MockBean
    LoanRepository loanRepository;
    @MockBean
    ReferenceRepository referenceRepository;
    @MockBean
    CodeCodeRepository codeCodeRepository;
    @Autowired
    FtpStorageProperties ftpStorageProperties;
    @Autowired
    private Environment env;


    @Test
    @DisplayName("when call api get loan detail success")
    public void testGetLoanDetailSuccess() {
        Loan loan = initData();
        Mockito.when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        ResponseEntity<BaseResponse<LoanDetailDTO>> result = loanDetailController.getDetail(1L);
        Assertions.assertEquals(Objects.requireNonNull(result.getBody()).getCode(), AppConstants.SUCCESS.getCode());
        Assertions.assertEquals(result.getBody().getMessage(), Translator.toLocale(AppConstants.SUCCESS.getMessage()));
    }

    @Test
    @DisplayName("when loan id not found")
    public void testGetLoanDetailFalse() {
        Mockito.when(loanRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        Exception exception = Assertions.assertThrows(LoanNotFoundException.class, () ->
                loanDetailController.getDetail(1L));
        String expectedMessage = "can not get loan by id.";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(actualMessage, expectedMessage);
    }


    @Test
    @DisplayName("when input id invalid")
    public void testUpdateToMBApprovalWithIdInvalid() {
        LoanConfirmRequestForm loanConfirmRequestForm = new LoanConfirmRequestForm();
        loanConfirmRequestForm.setId("abc");
        Exception exception = Assertions.assertThrows(NumberFormatException.class, () ->
                loanDetailController.updateToMBApproval(loanConfirmRequestForm));

        String expectedMessage = "For input string: \"abc\"";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(actualMessage, expectedMessage);

    }

    @Test
    @DisplayName("when update with id not found")
    public void testGetUpdateLoanFalse() {
        Mockito.when(loanRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        LoanConfirmRequestForm loanConfirmRequestForm = new LoanConfirmRequestForm();
        loanConfirmRequestForm.setId("2");

        Exception exception = Assertions.assertThrows(LoanNotFoundException.class, () ->
                loanDetailController.updateToMBApproval(loanConfirmRequestForm));

        String expectedMessage = "can not get loan by id.";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(actualMessage, expectedMessage);

    }

    @Test
    @DisplayName("connect to server fpt false")
    public void testUploadFileFalse() throws IOException {
        Loan loan = initData();
        Mockito.when(loanRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(loan));
        MockMultipartFile firstFile = new MockMultipartFile("data", "filename.pdf", "text/plain", "some xml".getBytes());
        ftpStorageProperties.setHost("fake");
        ftpStorageProperties.setPort("22");
        ftpStorageProperties.setPwd("fake");
        ftpStorageProperties.setUser("fake");
        ftpStorageProperties.setPwd("fake");
        ftpStorageProperties.setUploadDir("fake");

        LoanConfirmRequestForm loanConfirmRequestForm = new LoanConfirmRequestForm();
        loanConfirmRequestForm.setId("1");
        loanConfirmRequestForm.setFile(firstFile);

        Exception exception = Assertions.assertThrows(FtpException.class, () ->
                loanDetailController.updateToMBApproval(loanConfirmRequestForm));

        String expectedMessage = "com.jcraft.jsch.JSchException: java.net.ConnectException: Connection timed out: connect";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(actualMessage, expectedMessage);

    }

    @Test
    @DisplayName("Update Success")
    public void testUploadFileSuccess() throws Throwable {

        String host = env.getProperty("arpu.sftp.host");
        String port = env.getProperty("arpu.sftp.port");
        String user = env.getProperty("arpu.sftp.user");
        String pass = env.getProperty("arpu.sftp.pwd");
        String uploadDir = env.getProperty("arpu.sftp.uploadDir");
        String templateFile = env.getProperty("arpu.sftp.templateFile");

        ftpStorageProperties.setHost(host);
        ftpStorageProperties.setPort(port);
        ftpStorageProperties.setUser(user);
        ftpStorageProperties.setPwd(pass);
        ftpStorageProperties.setUploadDir(uploadDir);
        ftpStorageProperties.setTemplateFile(templateFile);

        Loan loan = initData();
        CodeCode codeCode = new CodeCode();
        codeCode.setId("PDS_02");
        Mockito.when(codeCodeRepository.findById("PDS_02")).thenReturn(Optional.of(codeCode));
        loan.setApprovalStatus(codeCode);
        Mockito.when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        MockMultipartFile firstFile = new MockMultipartFile("data", "filename.pdf", "text/plain", "some xml".getBytes());
        LoanConfirmRequestForm loanConfirmRequestForm = new LoanConfirmRequestForm();
        loanConfirmRequestForm.setId("1");
        loanConfirmRequestForm.setFile(firstFile);
        ResponseEntity<BaseResponse<UpdateLoanDTO>> entity = loanDetailController.updateToMBApproval(loanConfirmRequestForm);
        Assertions.assertEquals(Objects.requireNonNull(entity.getBody()).getCode(), AppConstants.SUCCESS.getCode());
        Assertions.assertEquals(entity.getBody().getMessage(), Translator.toLocale(AppConstants.SUCCESS.getMessage()));
    }

    @Test
    @DisplayName("Update File invalid")
    public void testUploadFileInvalid(){
        Loan loan = initData();
        CodeCode codeCode = new CodeCode();
        codeCode.setId("PDS_02");
        Mockito.when(codeCodeRepository.findById("PDS_02")).thenReturn(Optional.of(codeCode));
        loan.setApprovalStatus(codeCode);
        Mockito.when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
        LoanConfirmRequestForm loanConfirmRequestForm = new LoanConfirmRequestForm();
        loanConfirmRequestForm.setId("1");
        loanConfirmRequestForm.setFile(firstFile);

        Exception exception = Assertions.assertThrows(FileInvalidException.class, () ->
                loanDetailController.updateToMBApproval(loanConfirmRequestForm));

        String expectedMessage = "Must enter a file pdf";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(actualMessage, expectedMessage);

    }

    @Test
    @DisplayName("Update Success")
    public void testUpdateToMBApproval() throws Throwable {
        Loan loan = initData();
        loan.setId(2L);
        CodeCode codeCode = new CodeCode();
        codeCode.setId("PDS_02");
        Mockito.when(codeCodeRepository.findById("PDS_02")).thenReturn(Optional.of(codeCode));
        loan.setApprovalStatus(codeCode);
        Mockito.when(loanRepository.findById(2L)).thenReturn(Optional.of(loan));
        MockMultipartFile firstFile = new MockMultipartFile("data", "filename.pdf", "text/plain", "some xml".getBytes());
        LoanConfirmRequestForm loanConfirmRequestForm = new LoanConfirmRequestForm();
        loanConfirmRequestForm.setId("2");
        loanConfirmRequestForm.setFile(firstFile);
        ResponseEntity<BaseResponse<UpdateLoanDTO>> entity = loanDetailController.updateToMBApproval(loanConfirmRequestForm);
        Assertions.assertEquals(Objects.requireNonNull(entity.getBody()).getCode(), AppConstants.SUCCESS.getCode());
        Assertions.assertEquals(entity.getBody().getMessage(), Translator.toLocale(AppConstants.SUCCESS.getMessage()));
    }

    @Test
    @DisplayName("Update Success Loan Has Contract")
    public void testUpdateToMBApprovalWithLoanHasContract() throws Throwable {
        Loan loan = initData();
        loan.setId(10L);
        CodeCode codeCode = new CodeCode();
        codeCode.setId("PDS_02");
        Mockito.when(codeCodeRepository.findById("PDS_02")).thenReturn(Optional.of(codeCode));
        loan.setApprovalStatus(codeCode);
        Mockito.when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        LoanConfirmRequestForm loanConfirmRequestForm = new LoanConfirmRequestForm();
        loanConfirmRequestForm.setId("1");
        ResponseEntity<BaseResponse<UpdateLoanDTO>> entity = loanDetailController.updateToMBApproval(loanConfirmRequestForm);
        Assertions.assertEquals(Objects.requireNonNull(entity.getBody()).getCode(), AppConstants.SUCCESS.getCode());
        Assertions.assertEquals(entity.getBody().getMessage(), Translator.toLocale(AppConstants.SUCCESS.getMessage()));
    }


    public Loan initData() {
        Loan loan = new Loan();
        CodeCode codeCode = new CodeCode();
        Customer customer = new Customer();
        Address address = new Address();
        Set<CustomerRef> customerRefSet = new HashSet<>();
        Reference reference = new Reference();


        Interest interest = new Interest();
        interest.setId(1l);
        interest.setStatus("status");
        interest.setLoanTerm("loanTerm");
        interest.setInterestRate("intersRate");
        interest.setDescription("description");
        interest.setName("name");

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

        // init reference
        reference.setId(1L);
        reference.setEmail("Test@gmail.com");
        reference.setFullName("DaoVanDo");
        reference.setMsisdn("0977793056");

        // init data customerRef
        customerRef.setId(1L);
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
