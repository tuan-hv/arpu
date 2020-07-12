package com.viettel.arpu.service.mb.impl;

import com.viettel.arpu.MainApplication;
import com.viettel.arpu.config.MbStorageProperties;
import com.viettel.arpu.constant.enums.PayType;
import com.viettel.arpu.model.dto.mb.MbLoanLimitDTO;
import com.viettel.arpu.model.entity.CodeCode;
import com.viettel.arpu.model.entity.Customer;
import com.viettel.arpu.model.entity.Loan;
import com.viettel.arpu.model.entity.Reference;
import com.viettel.arpu.model.request.mb.MbChangeLimitForm;
import com.viettel.arpu.model.request.mb.MbConfirmCreateLoanForm;
import com.viettel.arpu.model.request.mb.MbConfirmLimitForm;
import com.viettel.arpu.model.request.mb.MbFinalLoanForm;
import com.viettel.arpu.model.request.mb.MbGetLimitForm;
import com.viettel.arpu.model.request.mb.MbHistoriesLoanForm;
import com.viettel.arpu.model.request.mb.MbLoanRegistrationForm;
import com.viettel.arpu.model.request.mb.MbOtpForm;
import com.viettel.arpu.model.request.mb.MbPayLoanForm;
import com.viettel.arpu.model.request.mb.MbPinForm;
import com.viettel.arpu.model.request.mb.MbVerifyOtpForm;
import com.viettel.arpu.model.response.mb.MbBaseResponse;
import com.viettel.arpu.model.response.mb.MbConfirmCreateLoanResponse;
import com.viettel.arpu.model.response.mb.MbHistoriesLoanResponse;
import com.viettel.arpu.model.response.mb.VtOtpConfirmResponse;
import com.viettel.arpu.model.response.mb.VtOtpDetailResponse;
import com.viettel.arpu.model.response.mb.VtOtpResponse;
import com.viettel.arpu.model.response.mb.VtPinResponse;
import com.viettel.arpu.model.response.mb.VtStatusResponse;
import com.viettel.arpu.model.response.mb.MbDetailLoanResponse;
import com.viettel.arpu.repository.CodeCodeRepository;
import com.viettel.arpu.repository.CustomerRepository;
import com.viettel.arpu.repository.LoanRepository;
import com.viettel.arpu.repository.ReferenceRepository;
import com.viettel.arpu.service.mb.MbRegisterLoanService;
import com.viettel.arpu.utils.GenerateRequestIdUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.spy;

@SpringBootTest(classes = MainApplication.class)
@RunWith(MockitoJUnitRunner.class)
class MbRegisterLoanServiceImplTest {
    @InjectMocks
    @Spy
    MbRegisterLoanServiceImpl mbRegisterLoanService = new MbRegisterLoanServiceImpl();

    @Autowired
    private MbRegisterLoanService service = new MbRegisterLoanServiceImpl();
    @Mock
    private LoanRepository loanRepository;

    @Mock
    private MbStorageProperties mbStorageProperties;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ReferenceRepository referenceRepository;

    @Mock
    private CodeCodeRepository codeCodeRepository;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private Environment env;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    Customer initCustomer() {
        Customer customer = new Customer();
        customer.setArpuLatestThreeMonths(BigDecimal.ONE);
        customer.setScoreMin(20);
        customer.setScoreMax(50);
        return customer;
    }

    MbLoanRegistrationForm initMBLoan() {
        MbLoanRegistrationForm mbLoanRegistrationForm = new MbLoanRegistrationForm();
        mbLoanRegistrationForm.setSourceMobile("0945668135");
        mbLoanRegistrationForm.setReferencerType("MQHS_01");
        mbLoanRegistrationForm.setPayType(PayType.PAY_TYPE_01);
        mbLoanRegistrationForm.setEmail("sourceMobile@gmail.com");
        mbLoanRegistrationForm.setBirthday(LocalDate.now());
        mbLoanRegistrationForm.setGender("1");
        mbLoanRegistrationForm.setNationality("vn");
        mbLoanRegistrationForm.setVillage("QH");
        mbLoanRegistrationForm.setDistrict("PC");
        mbLoanRegistrationForm.setProvince("HY");
        mbLoanRegistrationForm.setAddressDetail("QH,PC,HY");
        mbLoanRegistrationForm.setIdentityCardType("CMTND");
        mbLoanRegistrationForm.setIdentityCardNumber("034843994327");
        mbLoanRegistrationForm.setIssueDate(LocalDate.now());
        mbLoanRegistrationForm.setIssuePlace("HY");
        mbLoanRegistrationForm.setTerm("2");
        mbLoanRegistrationForm.setLoanType("1");
        mbLoanRegistrationForm.setAmount(new BigDecimal(10000000));
        mbLoanRegistrationForm.setFee(new BigDecimal(10000));
        mbLoanRegistrationForm.setReferencerName("HQV");
        mbLoanRegistrationForm.setReferencerMobile("84987655321");
        mbLoanRegistrationForm.setReferencerEmail("abcdeadsds@gmail.com");
        mbLoanRegistrationForm.setIsAutomaticPayment((byte)1);
        mbLoanRegistrationForm.setRequestId(GenerateRequestIdUtils.generateRequestId());
        return mbLoanRegistrationForm;
    }

    MbConfirmCreateLoanForm initCreateLoanForm() {
        MbConfirmCreateLoanForm mbConfirmCreateLoanForm = new MbConfirmCreateLoanForm();
        mbConfirmCreateLoanForm.setOtp("123456");
        return mbConfirmCreateLoanForm;
    }

    @Test
    void testShouldSendToMBApprove() {
        Customer customer = initCustomer();
        MbLoanRegistrationForm mbLoanRegistrationForm = initMBLoan();
        Mockito.when(customerRepository.findByMsisdn(mbLoanRegistrationForm.getSourceMobile())).thenReturn(Optional.of(customer));
        Mockito.when(mbStorageProperties.getCreateloan()).thenReturn("");
        MbBaseResponse mbBaseResponse = new MbBaseResponse();
        mbBaseResponse.setErrorCode("200");
        ResponseEntity responseEntity = new ResponseEntity(mbBaseResponse, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any())
        ).thenReturn(responseEntity);
        MbBaseResponse result = mbRegisterLoanService.sendToMBApprove(mbLoanRegistrationForm);
        Assertions.assertEquals("200", result.getErrorCode());
    }

    @Test
    void testShouldConfirmCreateLoanPass() {
        MbConfirmCreateLoanForm mbConfirmCreateLoanForm = initCreateLoanForm();
        Mockito.doReturn("123456").when(mbRegisterLoanService).getRequestId();
        MbLoanRegistrationForm mbLoanRegistrationForm = initMBLoan();
        Mockito.doReturn(mbLoanRegistrationForm).when(mbRegisterLoanService).getObjectFormInStorage("123456");
        Mockito.when(mbStorageProperties.getCreateloan()).thenReturn("");
        Customer customer = initCustomer();
        Mockito.when(customerRepository.findByMsisdn(mbLoanRegistrationForm.getSourceMobile()))
                .thenReturn(Optional.of(customer));
        Mockito.when(codeCodeRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.of(new CodeCode()));
        Mockito.when(referenceRepository.findByMsisdn(mbLoanRegistrationForm.getReferencerMobile()))
                .thenReturn(Optional.of(new Reference()));
        Mockito.when(env.getProperty("time.identifier")).thenReturn("2020-01-01");
        MbConfirmCreateLoanResponse mbConfirmCreateLoanResponse = new MbConfirmCreateLoanResponse();
        mbConfirmCreateLoanResponse.setErrorCode("00");
        mbConfirmCreateLoanResponse.setEndDate(LocalDateTime.now());
        ResponseEntity responseEntity = new ResponseEntity(mbConfirmCreateLoanResponse, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any())
        ).thenReturn(responseEntity);

        Assertions.assertEquals("00", mbRegisterLoanService.confirmCreateLoan(mbConfirmCreateLoanForm).getErrorCode());

    }

    @Test
    void testShouldConfirmCreateLoanPassWhenBeforeIdentifierDateTrueSuccess() {
        MbConfirmCreateLoanForm mbConfirmCreateLoanForm = initCreateLoanForm();
        Mockito.doReturn("123456").when(mbRegisterLoanService).getRequestId();
        MbLoanRegistrationForm mbLoanRegistrationForm = initMBLoan();
        Mockito.doReturn(mbLoanRegistrationForm).when(mbRegisterLoanService).getObjectFormInStorage("123456");
        Mockito.when(mbStorageProperties.getCreateloan()).thenReturn("");
        Customer customer = initCustomer();
        Mockito.when(customerRepository.findByMsisdn(mbLoanRegistrationForm.getSourceMobile()))
                .thenReturn(Optional.of(customer));
        Mockito.when(codeCodeRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.of(new CodeCode()));
        Mockito.when(referenceRepository.findByMsisdn(mbLoanRegistrationForm.getReferencerMobile()))
                .thenReturn(Optional.of(new Reference()));
        MbConfirmCreateLoanResponse mbConfirmCreateLoanResponse = new MbConfirmCreateLoanResponse();
        mbConfirmCreateLoanResponse.setErrorCode("00");
        mbConfirmCreateLoanResponse.setEndDate(LocalDateTime.now());
        mbRegisterLoanService.setIdentifierDateDefault("2070-01-01");
        ResponseEntity responseEntity = new ResponseEntity(mbConfirmCreateLoanResponse, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any())
        ).thenReturn(responseEntity);

        Assertions.assertEquals("00", mbRegisterLoanService.confirmCreateLoan(mbConfirmCreateLoanForm).getErrorCode());

    }

    @Test
    void testShouldConfirmCreateLoanFail() {
        MbConfirmCreateLoanForm mbConfirmCreateLoanForm = initCreateLoanForm();
        Mockito.doReturn("123456").when(mbRegisterLoanService).getRequestId();
        MbLoanRegistrationForm mbLoanRegistrationForm = initMBLoan();
        Mockito.doReturn(mbLoanRegistrationForm).when(mbRegisterLoanService).getObjectFormInStorage("123456");
        Mockito.when(mbStorageProperties.getCreateloan()).thenReturn("");
        MbConfirmCreateLoanResponse mbConfirmCreateLoanResponse = new MbConfirmCreateLoanResponse();
        mbConfirmCreateLoanResponse.setErrorCode("400");
        mbConfirmCreateLoanResponse.setEndDate(LocalDateTime.now());
        ResponseEntity responseEntity = new ResponseEntity(mbConfirmCreateLoanResponse, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any())
        ).thenReturn(responseEntity);

        Assertions.assertEquals("400", mbRegisterLoanService.confirmCreateLoan(mbConfirmCreateLoanForm).getErrorCode());

    }

    @Test
    void testGetHistories() {
        MbHistoriesLoanForm mbHistoriesLoanForm = new MbHistoriesLoanForm();
        mbHistoriesLoanForm.setRequestId("1234");
        MbHistoriesLoanResponse mbBaseResponse = new MbHistoriesLoanResponse();
        mbBaseResponse.setErrorCode("00");
        Mockito.when(mbStorageProperties.getHistoryloan()).thenReturn("");
        ResponseEntity responseEntity = new ResponseEntity(mbBaseResponse, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any())
        ).thenReturn(responseEntity);
        MbBaseResponse result = mbRegisterLoanService.getHistories(mbHistoriesLoanForm);
        Assertions.assertEquals("00", result.getErrorCode());
    }

    @Test
    void testShouldCheckLoanLimitWithCheckReduce() {
        MbChangeLimitForm mbChangeLimitForm = new MbChangeLimitForm();
        mbChangeLimitForm.setChangeAmount(BigDecimal.valueOf(15));
        mbChangeLimitForm.setId(1l);
        mbChangeLimitForm.setSourceMobile("0356447842");
        mbChangeLimitForm.setLoanAccount("0010701843008");
        mbChangeLimitForm.setIdentityCardType("CMTND");
        mbChangeLimitForm.setIdentityCardNumber("1123456788");
        mbChangeLimitForm.setSourceNumber("sourceNumber");
        Loan loanResult = new Loan();
        loanResult.setLoanAmount(BigDecimal.valueOf(20));
        MbBaseResponse mbBaseResponse = new MbBaseResponse();
        mbBaseResponse.setErrorCode("00");
        Mockito.when(mbStorageProperties.getReducelimit()).thenReturn("");
        ResponseEntity responseEntity = new ResponseEntity(mbBaseResponse, HttpStatus.OK);
        Mockito.when(loanRepository.findById(Long.valueOf(10))).thenReturn(Optional.of(loanResult));
        Mockito.doReturn("123456").when(mbRegisterLoanService).getRequestId();
        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any())
        ).thenReturn(responseEntity);
        MbBaseResponse result = mbRegisterLoanService.checkLoanLimit(mbChangeLimitForm, Long.valueOf(10));
        Assertions.assertEquals("00", result.getErrorCode());
    }

    @Test
    void testShouldCheckLoanLimitWithConfirmReduce() {
        MbChangeLimitForm mbChangeLimitForm = new MbChangeLimitForm();
        mbChangeLimitForm.setChangeAmount(BigDecimal.valueOf(25));
        mbChangeLimitForm.setRequestId(GenerateRequestIdUtils.generateRequestId());
        mbChangeLimitForm.setIncrease(false);
        Mockito.doReturn("123456").when(mbRegisterLoanService).getRequestId();
        Mockito.doReturn(mbChangeLimitForm).when(mbRegisterLoanService).getObjectFormInStorage("123456");
        MbBaseResponse mbBaseResponse = new MbBaseResponse();
        mbBaseResponse.setErrorCode("00");
        Mockito.when(mbStorageProperties.getReducelimit()).thenReturn("");
        ResponseEntity responseEntity = new ResponseEntity(mbBaseResponse, HttpStatus.OK);
        Mockito.doReturn("123456").when(mbRegisterLoanService).getRequestId();
        Mockito.when(loanRepository.findById(Mockito.any())).thenReturn(Optional.of(new Loan()));
        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any())
        ).thenReturn(responseEntity);

        MbConfirmLimitForm mbConfirmLimitForm = initMbConfirmLimitForm();
        MbBaseResponse result = mbRegisterLoanService.confirmLimit(mbConfirmLimitForm);
        Assertions.assertEquals("00", result.getErrorCode());
    }

    @Test
    void testShouldCheckLoanLimitWithCheckIncrease() {
        MbChangeLimitForm mbChangeLimitForm = new MbChangeLimitForm();
        mbChangeLimitForm.setChangeAmount(BigDecimal.valueOf(25));
        Loan loanResult = new Loan();
        loanResult.setLoanAmount(BigDecimal.valueOf(20));
        MbBaseResponse mbBaseResponse = new MbBaseResponse();
        mbBaseResponse.setErrorCode("00");
        Mockito.when(mbStorageProperties.getIncreaselimit()).thenReturn("");
        ResponseEntity responseEntity = new ResponseEntity(mbBaseResponse, HttpStatus.OK);
        Mockito.when(loanRepository.findById(Long.valueOf(10))).thenReturn(Optional.of(loanResult));
        Mockito.doReturn("123456").when(mbRegisterLoanService).getRequestId();
        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any())
        ).thenReturn(responseEntity);
        MbBaseResponse result = mbRegisterLoanService.checkLoanLimit(mbChangeLimitForm, Long.valueOf(10));
        Assertions.assertEquals("00", result.getErrorCode());
    }

    @Test
    void testShouldCheckLoanLimitWithConfirmIncrease() {
        MbChangeLimitForm mbChangeLimitForm = new MbChangeLimitForm();
        mbChangeLimitForm.setChangeAmount(BigDecimal.valueOf(25));
        mbChangeLimitForm.setRequestId(GenerateRequestIdUtils.generateRequestId());
        mbChangeLimitForm.setIncrease(true);
        Mockito.doReturn("123456").when(mbRegisterLoanService).getRequestId();
        Mockito.doReturn(mbChangeLimitForm).when(mbRegisterLoanService).getObjectFormInStorage("123456");
        MbBaseResponse mbBaseResponse = new MbBaseResponse();
        mbBaseResponse.setErrorCode("00");
        Mockito.when(mbStorageProperties.getIncreaselimit()).thenReturn("");
        ResponseEntity responseEntity = new ResponseEntity(mbBaseResponse, HttpStatus.OK);
        Mockito.doReturn("123456").when(mbRegisterLoanService).getRequestId();
        Mockito.when(loanRepository.findById(Mockito.any())).thenReturn(Optional.of(new Loan()));
        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any())
        ).thenReturn(responseEntity);

        MbConfirmLimitForm mbConfirmLimitForm = initMbConfirmLimitForm();
        MbBaseResponse result = mbRegisterLoanService.confirmLimit(mbConfirmLimitForm);
        Assertions.assertEquals("00", result.getErrorCode());
    }

    @Test
    void testShouldConfirmLimitFail() {
        MbChangeLimitForm mbChangeLimitForm = new MbChangeLimitForm();
        mbChangeLimitForm.setChangeAmount(BigDecimal.valueOf(25));
        mbChangeLimitForm.setRequestId(GenerateRequestIdUtils.generateRequestId());
        mbChangeLimitForm.setIncrease(true);
        Mockito.doReturn("123456").when(mbRegisterLoanService).getRequestId();
        Mockito.doReturn(mbChangeLimitForm).when(mbRegisterLoanService).getObjectFormInStorage("123456");
        MbBaseResponse mbBaseResponse = new MbBaseResponse();
        mbBaseResponse.setErrorCode("11");
        Mockito.when(mbStorageProperties.getIncreaselimit()).thenReturn("");
        ResponseEntity responseEntity = new ResponseEntity(mbBaseResponse, HttpStatus.OK);
        Mockito.doReturn("123456").when(mbRegisterLoanService).getRequestId();
        Mockito.when(loanRepository.findById(Mockito.any())).thenReturn(Optional.of(new Loan()));
        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any())
        ).thenReturn(responseEntity);

        MbConfirmLimitForm mbConfirmLimitForm = initMbConfirmLimitForm();
        MbBaseResponse result = mbRegisterLoanService.confirmLimit(mbConfirmLimitForm);
        Assertions.assertNotEquals("00", result.getErrorCode());
    }

    @Test
    void testShouldCheckPayLoanSuccess() {
        MbPayLoanForm mbPayLoanForm = new MbPayLoanForm();
        mbPayLoanForm.setRequestId("1234a");
        MbBaseResponse mbBaseResponse = new MbBaseResponse();
        mbBaseResponse.setErrorCode("00");
        ResponseEntity responseEntity = new ResponseEntity(mbBaseResponse, HttpStatus.OK);
        Mockito.when(mbStorageProperties.getPayloan()).thenReturn("");
        Mockito.doReturn("123456").when(mbRegisterLoanService).getRequestId();
        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any())
        ).thenReturn(responseEntity);

        MbBaseResponse result = mbRegisterLoanService.checkPayLoan(mbPayLoanForm);
        Assertions.assertEquals("00", result.getErrorCode());
    }

    @Test
    void testShouldCheckPayLoanFail() {
        MbPayLoanForm mbPayLoanForm = new MbPayLoanForm();
        mbPayLoanForm.setRequestId("1234a");
        MbBaseResponse mbBaseResponse = new MbBaseResponse();
        mbBaseResponse.setErrorCode("400");
        ResponseEntity responseEntity = new ResponseEntity(mbBaseResponse, HttpStatus.BAD_REQUEST);
        Mockito.when(mbStorageProperties.getPayloan()).thenReturn("");
        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any())
        ).thenReturn(responseEntity);

        MbBaseResponse result = mbRegisterLoanService.checkPayLoan(mbPayLoanForm);
        Assertions.assertEquals("400", result.getErrorCode());
    }

    @Test
    void testConfirmPayLoan() {
        MbPayLoanForm mbPayLoanForm = new MbPayLoanForm();
        mbPayLoanForm.setRequestId(GenerateRequestIdUtils.generateRequestId());
        Mockito.doReturn("123456").when(mbRegisterLoanService).getRequestId();
        Mockito.doReturn(mbPayLoanForm).when(mbRegisterLoanService).getObjectFormInStorage("123456");
        MbBaseResponse mbBaseResponse = new MbBaseResponse();
        mbBaseResponse.setErrorCode("00");
        ResponseEntity responseEntity = new ResponseEntity(mbBaseResponse, HttpStatus.OK);
        Mockito.when(mbStorageProperties.getPayloan()).thenReturn("");
        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any())
        ).thenReturn(responseEntity);

        MbConfirmLimitForm confirmLimitForm = initMbConfirmLimitForm();
        MbBaseResponse result = mbRegisterLoanService.confirmPayLoan(confirmLimitForm);
        Assertions.assertEquals("00", result.getErrorCode());
    }

    private MbConfirmLimitForm initMbConfirmLimitForm() {
        MbConfirmLimitForm confirmLimitForm = new MbConfirmLimitForm();
        confirmLimitForm.setRequestId("123456");
        confirmLimitForm.setOtp("123456");
        return confirmLimitForm;
    }

    @Test
    void testShouldCheckFinalLoanSuccess() {
        MbFinalLoanForm mbFinalLoanForm = new MbFinalLoanForm();
        mbFinalLoanForm.setRequestId("1234a");
        MbBaseResponse mbBaseResponse = new MbBaseResponse();
        mbBaseResponse.setErrorCode("00");
        ResponseEntity responseEntity = new ResponseEntity(mbBaseResponse, HttpStatus.OK);
        Mockito.when(mbStorageProperties.getFinalloan()).thenReturn("");
        Mockito.doReturn("123456").when(mbRegisterLoanService).getRequestId();
        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any())
        ).thenReturn(responseEntity);
        MbBaseResponse result = mbRegisterLoanService.checkFinalLoan(mbFinalLoanForm);
        Assertions.assertEquals("00", result.getErrorCode());
    }

    @Test
    void testShouldCheckFinalLoanFail() {
        MbFinalLoanForm mbFinalLoanForm = new MbFinalLoanForm();
        mbFinalLoanForm.setRequestId("1234a");
        MbBaseResponse mbBaseResponse = new MbBaseResponse();
        mbBaseResponse.setErrorCode("400");
        ResponseEntity responseEntity = new ResponseEntity(mbBaseResponse, HttpStatus.BAD_REQUEST);
        Mockito.when(mbStorageProperties.getFinalloan()).thenReturn("");
        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any())
        ).thenReturn(responseEntity);
        MbBaseResponse result = mbRegisterLoanService.checkFinalLoan(mbFinalLoanForm);
        Assertions.assertEquals("400", result.getErrorCode());
    }

    @Test
    void testConfirmFinalLoan() {
        MbFinalLoanForm mbFinalLoanForm = new MbFinalLoanForm();
        mbFinalLoanForm.setRequestId(GenerateRequestIdUtils.generateRequestId());
        Mockito.doReturn("123456").when(mbRegisterLoanService).getRequestId();
        Mockito.doReturn(mbFinalLoanForm).when(mbRegisterLoanService).getObjectFormInStorage("123456");
        MbBaseResponse mbBaseResponse = new MbBaseResponse();
        mbBaseResponse.setErrorCode("00");
        ResponseEntity responseEntity = new ResponseEntity(mbBaseResponse, HttpStatus.OK);
        Mockito.when(mbStorageProperties.getFinalloan()).thenReturn("");
        Mockito.doReturn("123456").when(mbRegisterLoanService).getRequestId();
        Mockito.when(loanRepository.findById(Mockito.any())).thenReturn(Optional.of(new Loan()));
        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any())
        ).thenReturn(responseEntity);

        MbBaseResponse result = mbRegisterLoanService.confirmFinalLoan(initMbConfirmLimitForm());
        Assertions.assertEquals("00", result.getErrorCode());
    }

    @Test
    void confirmFinalLoanFail() {
        MbFinalLoanForm mbFinalLoanForm = new MbFinalLoanForm();
        mbFinalLoanForm.setRequestId(GenerateRequestIdUtils.generateRequestId());
        Mockito.doReturn("123456").when(mbRegisterLoanService).getRequestId();
        Mockito.doReturn(mbFinalLoanForm).when(mbRegisterLoanService).getObjectFormInStorage("123456");
        MbBaseResponse mbBaseResponse = new MbBaseResponse();
        mbBaseResponse.setErrorCode("11");
        ResponseEntity responseEntity = new ResponseEntity(mbBaseResponse, HttpStatus.OK);
        Mockito.when(mbStorageProperties.getFinalloan()).thenReturn("");
        Mockito.doReturn("123456").when(mbRegisterLoanService).getRequestId();
        Mockito.when(loanRepository.findById(Mockito.any())).thenReturn(Optional.of(new Loan()));
        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any())
        ).thenReturn(responseEntity);
         MbBaseResponse result = mbRegisterLoanService.confirmFinalLoan(initMbConfirmLimitForm());
        Assertions.assertNotEquals("00", result.getErrorCode());
    }

    @Test
    void testShouldGetLoanLimitInMBWhenLoanAccountEmpty() {
        MbGetLimitForm mbGetLimitForm = new MbGetLimitForm();
        mbGetLimitForm.setSourceMobile("0945668135");
        mbGetLimitForm.setLoanAccount("");
        Customer customer = initCustomer();
        customer.setIdentityNumber("CMND");
        customer.setIdentityType("01");
        MbLoanLimitDTO mbLoanLimitDTO = new MbLoanLimitDTO();
        mbLoanLimitDTO.setErrorCode("11");
        ResponseEntity responseEntity = new ResponseEntity(mbLoanLimitDTO, HttpStatus.OK);
        Mockito.when(customerRepository.findByMsisdn(mbGetLimitForm.getSourceMobile())).thenReturn(Optional.of(customer));
        Mockito.when(mbStorageProperties.getCheckloan()).thenReturn("");
        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any())
        ).thenReturn(responseEntity);
        MbLoanLimitDTO result = mbRegisterLoanService.getLoanLimitInMB(mbGetLimitForm);
        Assertions.assertNotEquals("00", result.getErrorCode());
    }

    @Test
    void testShouldGetLoanLimitInMBWhenLoanAccountNotEmpty() {
        MbGetLimitForm mbGetLimitForm = new MbGetLimitForm();
        mbGetLimitForm.setSourceMobile("0945668135");
        mbGetLimitForm.setLoanAccount("test");
        Customer customer = initCustomer();
        customer.setIdentityNumber("CMND");
        customer.setIdentityType("00");
        MbLoanLimitDTO mbLoanLimitDTO = new MbLoanLimitDTO();
        mbLoanLimitDTO.setErrorCode("00");
        ResponseEntity responseFirstTime = new ResponseEntity(mbLoanLimitDTO, HttpStatus.OK);
        Mockito.when(customerRepository.findByMsisdn(mbGetLimitForm.getSourceMobile())).thenReturn(Optional.of(customer));
        Mockito.when(mbStorageProperties.getCheckloan()).thenReturn("");
        Mockito.when(mbStorageProperties.getDetailloan()).thenReturn("");

        MbDetailLoanResponse mbDetailLoanForm = new MbDetailLoanResponse();
        ResponseEntity responseSecondTime = new ResponseEntity(mbDetailLoanForm, HttpStatus.OK);

        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any())
        ).thenReturn(responseFirstTime).thenReturn(responseSecondTime);

        MbLoanLimitDTO result = mbRegisterLoanService.getLoanLimitInMB(mbGetLimitForm);
        Assertions.assertNotEquals("00", result.getErrorCode());
    }

    @Test
    void testValidatePinSuccess() {
        MbPinForm mbPinForm = new MbPinForm();
        mbPinForm.setPin("123456");
        mbPinForm.setProduct("VIETTELPAY");
        VtPinResponse mbBaseResponse = new VtPinResponse();
        VtStatusResponse vtStatusResponse = new VtStatusResponse();
        vtStatusResponse.setCode("00");
        vtStatusResponse.setMessage("test");
        mbBaseResponse.setVtStatusResponse(vtStatusResponse);
        Mockito.when(mbStorageProperties.getValidatePin()).thenReturn("");
        ResponseEntity responseEntity = new ResponseEntity(mbBaseResponse, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any())
        ).thenReturn(responseEntity);
        MbBaseResponse result = mbRegisterLoanService.validatePin(mbPinForm);
        then(result.getErrorCode()).isEqualTo("00");
    }

    @Test
    void testValidateOtp() {
        MbOtpForm mbOtpForm = new MbOtpForm();
        mbOtpForm.setRequestId(GenerateRequestIdUtils.generateRequestId());
        mbOtpForm.setProduct("VIETTELPAY");
        VtOtpResponse mbBaseResponse = new VtOtpResponse();
        VtOtpDetailResponse vtOtpDetailResponse = new VtOtpDetailResponse();
        vtOtpDetailResponse.setOtp("123456");
        vtOtpDetailResponse.setSignedRequest("");
        vtOtpDetailResponse.setToken("");
        mbBaseResponse.setVtOtpDetailResponse(vtOtpDetailResponse);
        VtStatusResponse vtStatusResponse = new VtStatusResponse();
        vtStatusResponse.setCode("00");
        vtStatusResponse.setMessage("test");
        mbBaseResponse.setVtStatusResponse(vtStatusResponse);
        Mockito.when(mbStorageProperties.getValidatePin()).thenReturn("");
        ResponseEntity responseEntity = new ResponseEntity(mbBaseResponse, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any())
        ).thenReturn(responseEntity);
        MbBaseResponse result = mbRegisterLoanService.validateOtp(mbOtpForm);
        then(result.getErrorCode()).isEqualTo("00");
    }

    @Test
    void testValidateOtpFail() {
        MbOtpForm mbOtpForm = new MbOtpForm();
        mbOtpForm.setRequestId(GenerateRequestIdUtils.generateRequestId());
        mbOtpForm.setProduct("VIETTELPAY");
        VtOtpResponse mbBaseResponse = new VtOtpResponse();
        VtOtpDetailResponse vtOtpDetailResponse = new VtOtpDetailResponse();
        vtOtpDetailResponse.setOtp("123456");
        vtOtpDetailResponse.setSignedRequest("");
        vtOtpDetailResponse.setToken("");
        mbBaseResponse.setVtOtpDetailResponse(vtOtpDetailResponse);
        VtStatusResponse vtStatusResponse = new VtStatusResponse();
        vtStatusResponse.setCode("11");
        vtStatusResponse.setMessage("test");
        mbBaseResponse.setVtStatusResponse(vtStatusResponse);
        Mockito.when(mbStorageProperties.getValidatePin()).thenReturn("");
        ResponseEntity responseEntity = new ResponseEntity(mbBaseResponse, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any())
        ).thenReturn(responseEntity);
        MbBaseResponse result = mbRegisterLoanService.validateOtp(mbOtpForm);
        then(result.getErrorCode()).isEqualTo("11");
    }

    @Test
    void testVerifyOtp() {
        MbVerifyOtpForm mbVerifyOtpForm = new MbVerifyOtpForm();
        mbVerifyOtpForm.setRequestId(GenerateRequestIdUtils.generateRequestId());
        mbVerifyOtpForm.setOtp("123456");
        mbVerifyOtpForm.setSignedRequest("");
        VtOtpConfirmResponse mbBaseResponse = new VtOtpConfirmResponse();
        VtStatusResponse vtStatusResponse = new VtStatusResponse();
        vtStatusResponse.setCode("00");
        vtStatusResponse.setMessage("test");
        mbBaseResponse.setVtStatusResponse(vtStatusResponse);
        Mockito.when(mbStorageProperties.getValidatePin()).thenReturn("");
        ResponseEntity responseEntity = new ResponseEntity(mbBaseResponse, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any())
        ).thenReturn(responseEntity);
        MbBaseResponse result = mbRegisterLoanService.verifyOtp(mbVerifyOtpForm);
        then(result.getErrorCode()).isEqualTo("00");
    }
}
