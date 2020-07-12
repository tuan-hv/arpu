package com.viettel.arpu.controller.mobile;

import com.viettel.arpu.exception.LoanNotFoundException;
import com.viettel.arpu.exception.MbOtpFailException;
import com.viettel.arpu.exception.WithdrawMoneyException;
import com.viettel.arpu.exception.MbOtpFailException;
import com.viettel.arpu.exception.WithdrawMoneyException;
import com.viettel.arpu.model.dto.WithdrawMoneyDTO;
import com.viettel.arpu.model.request.WithdrawMoneyForm;
import com.viettel.arpu.model.response.BaseResponse;
import com.viettel.arpu.model.response.mb.VtStatusResponse;
import com.viettel.arpu.service.loan.MobileService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MobileLoanControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private MobileService mobileService;

    @InjectMocks
    private MobileLoanController mbMobileLoanController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testShouldGetAllLoanSuccess() throws Exception {
        this.mockMvc.perform(get("/api/mb/loans")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .param("phoneNumber", "0945668135"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Thành công.")));
    }

    @Test
    void testShouldGetAllLoanFailWithInvalidPhone() throws Exception {
        this.mockMvc.perform(get("/api/mb/loans")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .param("phoneNumber", ""))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("số điện thoại không được để trống")));
    }

    @Test
    void testShouldGetExistCustomerSuccess() throws Exception {
        this.mockMvc.perform(get("/api/mb/loans/customers/exist")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .param("phoneNumber", "0945668135"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Thành công.")));
    }

    @Test
    void testShouldGetExistCustomerFailWithEmptyPhone() throws Exception {
        this.mockMvc.perform(get("/api/mb/loans/customers/exist")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .param("phoneNumber", ""))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("số điện thoại không được để trống")));
    }

    @Test
    void shouldGetRelationshipCustomerSuccess() throws Exception {
        this.mockMvc.perform(get("/api/mb/loans/customers/reference")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .param("phoneNumber", "0356447840"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Thành công.")));
    }

    @Test
    void testShouldGetRelationshipCustomerFailWithEmptyPhone() throws Exception {
        this.mockMvc.perform(get("/api/mb/loans/customers/reference")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .param("phoneNumber", ""))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("số điện thoại không được để trống")));
    }

    @Test

    void testWithDrawnShouldReturnSuccessIfAllIsValidated() {
        WithdrawMoneyForm withdrawMoneyForm = new WithdrawMoneyForm();
        withdrawMoneyForm.setAmount("500");
        withdrawMoneyForm.setBankAccount("");
        withdrawMoneyForm.setOtp("123456");
        withdrawMoneyForm.setSourceMobile("123456789");
        WithdrawMoneyDTO withdrawMoneyDTO = new WithdrawMoneyDTO();
        withdrawMoneyDTO.setAmount("500");
        withdrawMoneyDTO.setBankAccount("");
        withdrawMoneyDTO.setSourceMobile("123456789");
        VtStatusResponse vtStatusResponse = new VtStatusResponse();
        vtStatusResponse.setCode("00");
        vtStatusResponse.setMessage("test");
        Mockito.when(mobileService.withdrawMoneyToVTP(withdrawMoneyForm)).thenReturn(withdrawMoneyDTO);
        ResponseEntity<BaseResponse<WithdrawMoneyDTO>> entity = mbMobileLoanController.withdrawMoney(withdrawMoneyForm);
        assertEquals(entity.getStatusCode().value(), 200);
        assertEquals(entity.getBody().getCode(), "ARPU200");
    }

    @Test
    void testWithDrawnShouldGetWithdrawMoneyExceptionIfHasError() {
        WithdrawMoneyForm withdrawMoneyForm = new WithdrawMoneyForm();
        withdrawMoneyForm.setAmount("500");
        withdrawMoneyForm.setBankAccount("");
        withdrawMoneyForm.setOtp("123456");
        withdrawMoneyForm.setSourceMobile("123456789");
        Mockito.when(mobileService.withdrawMoneyToVTP(withdrawMoneyForm)).thenThrow(new WithdrawMoneyException());
        Exception exception = Assertions.assertThrows(WithdrawMoneyException.class, () ->
                mbMobileLoanController.withdrawMoney(withdrawMoneyForm));
        String expectedMessage = "Giao dịch rút tiền thất bại";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testWithDrawnShouldGetMbOtpFailExceptionIfWrongOTP() {
        WithdrawMoneyForm withdrawMoneyForm = new WithdrawMoneyForm();
        withdrawMoneyForm.setAmount("500");
        withdrawMoneyForm.setBankAccount("");
        withdrawMoneyForm.setOtp("123456");
        withdrawMoneyForm.setSourceMobile("123456789");
        Mockito.when(mobileService.withdrawMoneyToVTP(withdrawMoneyForm)).thenThrow(new MbOtpFailException());
        Exception exception = Assertions.assertThrows(MbOtpFailException.class, () ->
                mbMobileLoanController.withdrawMoney(withdrawMoneyForm));
        String expectedMessage = "OTP fail.";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);

    }

}
