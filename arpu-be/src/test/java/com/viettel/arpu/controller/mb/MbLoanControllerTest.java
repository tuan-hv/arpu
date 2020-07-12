package com.viettel.arpu.controller.mb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.arpu.constant.enums.PayType;
import com.viettel.arpu.model.dto.mb.MbLoanLimitDTO;
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
import com.viettel.arpu.model.response.mb.MbHistoriesLoanResponse;
import com.viettel.arpu.service.mb.MbRegisterLoanService;
import com.viettel.arpu.utils.GenerateRequestIdUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @Author VuHQ
 * @Since 6/30/2020
 */
@SpringBootTest
@AutoConfigureMockMvc
public class MbLoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    MbRegisterLoanService mbRegisterLoanService;

    @Test
    void shouldSendToMBApproval() throws Exception {
        MbLoanRegistrationForm mbLoanRegistrationForm = new MbLoanRegistrationForm();
        mbLoanRegistrationForm.setSourceMobile("84963807844");
        mbLoanRegistrationForm.setEmail("sourceMobile@gmail.com");
        mbLoanRegistrationForm.setBirthday(LocalDate.parse("01/08/1993", DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        mbLoanRegistrationForm.setGender("1");
        mbLoanRegistrationForm.setNationality("vn");
        mbLoanRegistrationForm.setVillage("QH");
        mbLoanRegistrationForm.setDistrict("PC");
        mbLoanRegistrationForm.setProvince("HY");
        mbLoanRegistrationForm.setAddressDetail("QH,PC,HY");
        mbLoanRegistrationForm.setIdentityCardType("CMND");
        mbLoanRegistrationForm.setIdentityCardNumber("034843994327");
        mbLoanRegistrationForm.setIssueDate(LocalDate.parse("01/08/2007", DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        mbLoanRegistrationForm.setIssuePlace("HY");
        mbLoanRegistrationForm.setTerm("2");
        mbLoanRegistrationForm.setPayType(PayType.PAY_TYPE_02);
        mbLoanRegistrationForm.setLoanType("1");
        mbLoanRegistrationForm.setAmount(new BigDecimal(10000000));
        mbLoanRegistrationForm.setFee(new BigDecimal(10000));
        mbLoanRegistrationForm.setReferencerName("HQV");
        mbLoanRegistrationForm.setReferencerType("MQHS_01");
        mbLoanRegistrationForm.setReferencerMobile("84987655321");
        mbLoanRegistrationForm.setReferencerEmail("abcdeadsds@gmail.com");
        mbLoanRegistrationForm.setIsAutomaticPayment((byte)1);
        mbLoanRegistrationForm.toString();
        MbBaseResponse response = new MbBaseResponse();

        ObjectMapper objectMapper = new ObjectMapper();

        Mockito.when(mbRegisterLoanService.sendToMBApprove(mbLoanRegistrationForm)).thenReturn(response);

        this.mockMvc.perform(post("/api/mb/loans/approval")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mbLoanRegistrationForm)))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.message", is("yêu cầu không hợp lệ.")))
                        .andExpect(jsonPath("$.code", is("ARPU400")));

    }

    @Test
    void shouldSendToMBConfirm() throws Exception {
        MbConfirmCreateLoanForm mbConfirmCreateLoanForm = new MbConfirmCreateLoanForm();
        mbConfirmCreateLoanForm.setLoanRequestId("123456789");
        mbConfirmCreateLoanForm.setOtp("123456");
        mbConfirmCreateLoanForm.setSourceMobile("0921548784");
        mbConfirmCreateLoanForm.setSourceNumber("1234567898");
        MbBaseResponse response = new MbBaseResponse();
        response.setErrorCode("200");
        mbConfirmCreateLoanForm.toString();
        ObjectMapper objectMapper = new ObjectMapper();

        Mockito.when(mbRegisterLoanService.confirmCreateLoan(Mockito.any(MbConfirmCreateLoanForm.class))).thenReturn(response);

        this.mockMvc.perform(post("/api/mb/loans/confirms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mbConfirmCreateLoanForm)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Thành công.")))
                .andExpect(jsonPath("$.code", is("ARPU200")));

    }

    @Test
    void shouldGetLoanHistoriesInMB() throws Exception {
        MbHistoriesLoanForm historiesLoanForm = new MbHistoriesLoanForm();
        historiesLoanForm.setLoanAccount("123123123");
        historiesLoanForm.setPin("123456");
        historiesLoanForm.setSourceMobile("123456789");
        historiesLoanForm.toString();
        MbHistoriesLoanResponse response = new MbHistoriesLoanResponse();
        response.setErrorCode("200");
        Mockito.when(mbRegisterLoanService.getHistories(Mockito.any(MbHistoriesLoanForm.class))).thenReturn(response);

        ObjectMapper objectMapper = new ObjectMapper();
        this.mockMvc.perform(post("/api/mb/loans/histories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(historiesLoanForm)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Thành công.")))
                .andExpect(jsonPath("$.code", is("ARPU200")));
    }

    @Test
    void shouldGetLoanLimitInMB() throws Exception {
        MbGetLimitForm mbGetLimitForm = new MbGetLimitForm();
        mbGetLimitForm.setLoanType("123456789");
        mbGetLimitForm.setSourceMobile("123456789");
        mbGetLimitForm.setSourceNumber("214578541");
        mbGetLimitForm.setRequestId("123548742145");
        MbLoanLimitDTO response = new MbLoanLimitDTO();
        mbGetLimitForm.toString();
        Mockito.when(mbRegisterLoanService.getLoanLimitInMB(Mockito.any(MbGetLimitForm.class))).thenReturn(response);
        ObjectMapper objectMapper = new ObjectMapper();
        this.mockMvc.perform(post("/api/mb/loans/customers/limit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mbGetLimitForm)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("yêu cầu không hợp lệ.")))
                .andExpect(jsonPath("$.code", is("ARPU400")));
    }

    @Test
    void checkLoanLimitInMB() throws Exception {
        MbChangeLimitForm mbChangeLimitForm = new MbChangeLimitForm();
        mbChangeLimitForm.setChangeAmount(new BigDecimal(5000000));
        mbChangeLimitForm.setIdentityCardNumber("123123123");
        mbChangeLimitForm.setIdentityCardType("CMND");
        mbChangeLimitForm.setSourceMobile("09382727");
        mbChangeLimitForm.setLoanAccount("131231231231");
        mbChangeLimitForm.setSourceNumber("1312312312");
        mbChangeLimitForm.setId(new Long(13));
        mbChangeLimitForm.toString();
        MbBaseResponse response = new MbBaseResponse();
        Mockito.when(mbRegisterLoanService.checkLoanLimit(Mockito.any(MbChangeLimitForm.class), Mockito.any())).thenReturn(response);
        ObjectMapper objectMapper = new ObjectMapper();
        this.mockMvc.perform(post("/api/mb/loans/limit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mbChangeLimitForm)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Thành công.")))
                .andExpect(jsonPath("$.code", is("ARPU200")));
    }

    @Test
    void confirmChangeLimitInMB() throws Exception {
        MbConfirmLimitForm mbConfirmLimitForm = new MbConfirmLimitForm();
        mbConfirmLimitForm.setOtp("123456");
        mbConfirmLimitForm.setRequestId("123456");
        mbConfirmLimitForm.toString();
        MbBaseResponse response = new MbBaseResponse();
        response.setErrorCode("ARPU200");
        response.setErrorDesc("123123");
        Mockito.when(mbRegisterLoanService.confirmLimit(Mockito.any(MbConfirmLimitForm.class))).thenReturn(response);
        ObjectMapper objectMapper = new ObjectMapper();
        this.mockMvc.perform(put("/api/mb/loans/limit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mbConfirmLimitForm)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Thành công.")))
                .andExpect(jsonPath("$.code", is("ARPU200")));
    }

    @Test
    void checkPayLoanInMB() throws Exception {
        MbPayLoanForm mbPayLoanForm = new MbPayLoanForm();
        mbPayLoanForm.setAmount(new BigDecimal(100000));
        mbPayLoanForm.setId(12345l);
        mbPayLoanForm.setLoanAccount("loanAccount");
        mbPayLoanForm.setSourceNumber("123456789");
        mbPayLoanForm.setSourceMobile("1234567");
        mbPayLoanForm.toString();
        MbBaseResponse response = new MbBaseResponse();
        ObjectMapper objectMapper = new ObjectMapper();
        Mockito.when(mbRegisterLoanService.checkPayLoan(Mockito.any(MbPayLoanForm.class))).thenReturn(new MbBaseResponse());
        this.mockMvc.perform(post("/api/mb/loans/pay")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mbPayLoanForm)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Thành công.")))
                .andExpect(jsonPath("$.code", is("ARPU200")));
    }

    @Test
    void confirmPayLoanInMB() throws Exception {
        MbConfirmLimitForm mbConfirmLimitForm = new MbConfirmLimitForm();
        mbConfirmLimitForm.setOtp("123456");
        mbConfirmLimitForm.toString();
        MbBaseResponse response = new MbBaseResponse();
        Mockito.when(mbRegisterLoanService.confirmPayLoan(Mockito.any(MbConfirmLimitForm.class))).thenReturn(response);
        ObjectMapper objectMapper = new ObjectMapper();
        this.mockMvc.perform(put("/api/mb/loans/pay")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mbConfirmLimitForm)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Thành công.")))
                .andExpect(jsonPath("$.code", is("ARPU200")));
    }

    @Test
    void checkFinalLoanInMB() throws Exception {
        MbFinalLoanForm mbFinalLoanForm = new MbFinalLoanForm();
        mbFinalLoanForm.setId(123l);
        mbFinalLoanForm.setLoanAccount("13123123123");
        mbFinalLoanForm.setSourceMobile("13131312");
        mbFinalLoanForm.setSourceNumber("13123121");
        mbFinalLoanForm.setRequestId("requestId");
        MbBaseResponse response = new MbBaseResponse();
        Mockito.when(mbRegisterLoanService.checkFinalLoan(Mockito.any(MbFinalLoanForm.class))).thenReturn(response);
        ObjectMapper objectMapper = new ObjectMapper();
        this.mockMvc.perform(post("/api/mb/loans/final")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mbFinalLoanForm)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Thành công.")))
                .andExpect(jsonPath("$.code", is("ARPU200")));
    }

    @Test
    void confirmFinalLoanInMB() throws Exception {
        MbConfirmLimitForm mbConfirmLimitForm = new MbConfirmLimitForm();
        mbConfirmLimitForm.setRequestId("13123123123");
        mbConfirmLimitForm.setOtp("123456");
        mbConfirmLimitForm.toString();
        MbBaseResponse response = new MbBaseResponse();
        Mockito.when(mbRegisterLoanService.confirmFinalLoan(Mockito.any(MbConfirmLimitForm.class))).thenReturn(response);
        ObjectMapper objectMapper = new ObjectMapper();
        this.mockMvc.perform(put("/api/mb/loans/final")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mbConfirmLimitForm)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Thành công.")))
                .andExpect(jsonPath("$.code", is("ARPU200")));
    }

    @Test
    void validatePin() throws Exception {
        MbPinForm mbPinForm = new MbPinForm();
        mbPinForm.setAppToken("appToken");
        mbPinForm.setMsisdn("84346655234");
        mbPinForm.setOtherProp("otherProp");
        mbPinForm.setPin("123465");
        mbPinForm.setProduct("product");
        mbPinForm.setServiceName("serviceName");
        mbPinForm.setRequestId("requestId");
        mbPinForm.toString();
        MbBaseResponse response = new MbBaseResponse();
        Mockito.when(mbRegisterLoanService.validatePin(Mockito.any(MbPinForm.class))).thenReturn(response);
        ObjectMapper objectMapper = new ObjectMapper();
        this.mockMvc.perform(post("/api/mb/loans/pin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mbPinForm)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Thành công.")))
                .andExpect(jsonPath("$.code", is("ARPU200")));
    }

    @Test
    void createOtpShouldReturnSuccessIfAllIsValidated() throws Exception {
        MbOtpForm mbOtpForm = new MbOtpForm();
        mbOtpForm.setRequestId(GenerateRequestIdUtils.generateRequestId());
        mbOtpForm.setProduct("VIETTELPAY");
        mbOtpForm.setMsisdn("123456789");
        mbOtpForm.setOtherProp("otherProp");
        mbOtpForm.setProduct("product");
        mbOtpForm.setAppToken("appToken");
        mbOtpForm.setServiceName("serviceName");
        mbOtpForm.toString();
        MbBaseResponse mbBaseResponse = new MbBaseResponse();
        mbBaseResponse.setErrorCode("00");
        mbBaseResponse.setErrorDesc("test");
        Mockito.when(mbRegisterLoanService.validateOtp(Mockito.any(MbOtpForm.class))).thenReturn(mbBaseResponse);
        ObjectMapper objectMapper = new ObjectMapper();
        this.mockMvc.perform(post("/api/mb/loans/otp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mbOtpForm)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Thành công.")))
                .andExpect(jsonPath("$.code", is("ARPU200")));
    }

    @Test
    void verifyOtpShouldReturnSuccessIfAllIsValidated() throws Exception {
        MbVerifyOtpForm mbVerifyOtpForm = new MbVerifyOtpForm();
        mbVerifyOtpForm.setRequestId(GenerateRequestIdUtils.generateRequestId());
        mbVerifyOtpForm.setSignedRequest("VIETTELPAY");
        mbVerifyOtpForm.setOtp("123456");
        mbVerifyOtpForm.toString();
        MbBaseResponse mbBaseResponse = new MbBaseResponse();
        mbBaseResponse.setErrorCode("00");
        mbBaseResponse.setErrorDesc("test");
        Mockito.when(mbRegisterLoanService.verifyOtp(Mockito.any(MbVerifyOtpForm.class))).thenReturn(mbBaseResponse);
        ObjectMapper objectMapper = new ObjectMapper();
        this.mockMvc.perform(post("/api/mb/loans/otp/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mbVerifyOtpForm)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Thành công.")))
                .andExpect(jsonPath("$.code", is("ARPU200")));
    }
}
