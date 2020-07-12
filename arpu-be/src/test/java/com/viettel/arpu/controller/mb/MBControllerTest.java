/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */

package com.viettel.arpu.controller.mb;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MBControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    void testReturnBadRequestWhenAPIWitdIdInvalid() throws Exception {
        this.mockMvc.perform(put("/api/mb/loans/loan_approval")
                .param("loanId", "A")
                .param("codeId", "PDS_04")
                .param("reason", "Ly do tu choi")
                .param("sourceMobile", "09875484494")
                .param("sourceNumber", "0997487545")
                .param("identityCardType", "009494994914")
                .param("identityCardNumber", "009494994914")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Id phải truyền vào dạng số.")))
                .andExpect(jsonPath("$.message", is("yêu cầu không hợp lệ.")))
                .andExpect(jsonPath("$.code", is("ARPU400")));
    }

    @Test
    void testReturnNotFoundWhenAPIGetLoanNotFound() throws Exception {
        this.mockMvc.perform(put("/api/mb/loans/loan_approval")
                .param("loanId", "1")
                .param("codeId", "PDS_0")
                .param("reason", "Ly do tu choi")
                .param("sourceMobile", "09875484494")
                .param("sourceNumber", "0997487545")
                .param("identityCardType", "009494994914")
                .param("identityCardNumber", "009494994914")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Không tìm thấy khoản vay thỏa mãn.")))
                .andExpect(jsonPath("$.code", is("ARPU405")));
    }

    @Test
    void testReturnBadRequestWhenAPIWitdLoanHasBeenApprovedOrRejected() throws Exception {
        this.mockMvc.perform(put("/api/mb/loans/loan_approval")
                .param("loanId", "2")
                .param("codeId", "PDS_03")
                .param("reason", "Ly do tu choi")
                .param("sourceMobile", "09875484494")
                .param("sourceNumber", "0997487545")
                .param("identityCardType", "009494994914")
                .param("identityCardNumber", "009494994914")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Khoản vay đã được phê duyệt hoặc từ chối")))
                .andExpect(jsonPath("$.code", is("ARPU400")));
    }

    @Test
    void testReturnBadRequestWhenAPIWitdCodeInvalid() throws Exception {
        this.mockMvc.perform(put("/api/mb/loans/loan_approval")
                .param("loanId", "10")
                .param("codeId", "PDS_00")
                .param("reason", "Ly do tu choi")
                .param("sourceMobile", "09875484494")
                .param("sourceNumber", "0997487545")
                .param("identityCardType", "009494994914")
                .param("identityCardNumber", "009494994914")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Code truyền vào không hợp lệ")))
                .andExpect(jsonPath("$.code", is("ARPU400")));
    }

    @Test
    void testReturnBadRequestWhenAPIWitdSourceMobileInvalid() throws Exception {
        this.mockMvc.perform(put("/api/mb/loans/loan_approval")
                .param("loanId", "10")
                .param("codeId", "PDS_03")
                .param("reason", "Ly do tu choi")
                .param("sourceMobile", "aaaaaaaa")
                .param("sourceNumber", "0997487545")
                .param("identityCardType", "009494994914")
                .param("identityCardNumber", "009494994914")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("yêu cầu không hợp lệ.")))
                .andExpect(jsonPath("$.code", is("ARPU400")));
    }

    @Test
    void testReturnBadRequestWhenAPIWitdSourceMobileSizeOutOfRange() throws Exception {
        this.mockMvc.perform(put("/api/mb/loans/loan_approval")
                .param("loanId", "10")
                .param("codeId", "PDS_03")
                .param("reason", "Ly do tu choi")
                .param("sourceMobile", "99999999999999")
                .param("sourceNumber", "0997487545")
                .param("identityCardType", "009494994914")
                .param("identityCardNumber", "009494994914")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("yêu cầu không hợp lệ.")))
                .andExpect(content().string(containsString("sourceMobile phải có độ dài từ 10 đến 12 kí tự.")))
                .andExpect(jsonPath("$.code", is("ARPU400")));
    }

    @Test
    void testReturnBadRequestWhenAPIWithSourceNumberInvalid() throws Exception {
        this.mockMvc.perform(put("/api/mb/loans/loan_approval")
                .param("loanId", "10")
                .param("codeId", "PDS_03")
                .param("reason", "Ly do tu choi")
                .param("sourceMobile", "009494994914")
                .param("sourceNumber", "aaaaaaaaaaa")
                .param("identityCardType", "009494994914")
                .param("identityCardNumber", "009494994914")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("yêu cầu không hợp lệ.")))
                .andExpect(content().string(containsString("sourceNumber phải truyền vào dạng số.")))
                .andExpect(jsonPath("$.code", is("ARPU400")));
    }

    @Test
    void testReturnBadRequestWhenAPIWithIdentityCardNumberInvalid() throws Exception {
        this.mockMvc.perform(put("/api/mb/loans/loan_approval")
                .param("loanId", "10")
                .param("codeId", "PDS_03")
                .param("reason", "Ly do tu choi")
                .param("sourceMobile", "009494994914")
                .param("sourceNumber", "009494994914")
                .param("identityCardType", "009494994914")
                .param("identityCardNumber", "aaaaaaaaaaaaaa")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("yêu cầu không hợp lệ.")))
                .andExpect(content().string(containsString("identityCardNumber phải truyền vào dạng số.")))
                .andExpect(jsonPath("$.code", is("ARPU400")));
    }

    @Test
    void testReturnBadRequestWhenAPIWithDataNotMatch() throws Exception {
        this.mockMvc.perform(put("/api/mb/loans/loan_approval")
                .param("loanId", "10")
                .param("codeId", "PDS_03")
                .param("reason", "Ly do tu choi")
                .param("sourceMobile", "009494994914")
                .param("sourceNumber", "009494994914")
                .param("identityCardType", "009494994914")
                .param("identityCardNumber", "009494994914")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Dữ liệu truyền vào không trùng khớp trong database")))
                .andExpect(jsonPath("$.code", is("ARPU400")));
    }

    @Test
    void testReturnBadRequestWhenAPIWithIdentityCardTypeNotMatch() throws Exception {
        this.mockMvc.perform(put("/api/mb/loans/loan_approval")
                .param("loanId", "10")
                .param("codeId", "PDS_03")
                .param("reason", "Ly do tu choi")
                .param("sourceMobile", "0395954111")
                .param("sourceNumber", "3")
                .param("identityCardType", "CCCD")
                .param("identityCardNumber", "24354545")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Dữ liệu truyền vào không trùng khớp trong database")))
                .andExpect(jsonPath("$.code", is("ARPU400")));
    }

    @Test
    void testReturnBadRequestWhenAPIWithSourceMobileNotMatch() throws Exception {
        this.mockMvc.perform(put("/api/mb/loans/loan_approval")
                .param("loanId", "10")
                .param("codeId", "PDS_03")
                .param("reason", "Ly do tu choi")
                .param("sourceMobile", "0395954110")
                .param("sourceNumber", "3")
                .param("identityCardType", "CMTND")
                .param("identityCardNumber", "24354545")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Dữ liệu truyền vào không trùng khớp trong database")))
                .andExpect(jsonPath("$.code", is("ARPU400")));
    }

    @Test
    void testReturnBadRequestWhenAPIWithSourceNumberNotMatch() throws Exception {
        this.mockMvc.perform(put("/api/mb/loans/loan_approval")
                .param("loanId", "10")
                .param("codeId", "PDS_03")
                .param("reason", "Ly do tu choi")
                .param("sourceMobile", "0395954111")
                .param("sourceNumber", "4")
                .param("identityCardType", "CMTND")
                .param("identityCardNumber", "24354545")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Dữ liệu truyền vào không trùng khớp trong database")))
                .andExpect(jsonPath("$.code", is("ARPU400")));
    }

    @Test
    void testReturnBadRequestWhenAPIWithIdentityCardNumberNotMatch() throws Exception {
        this.mockMvc.perform(put("/api/mb/loans/loan_approval")
                .param("loanId", "10")
                .param("codeId", "PDS_03")
                .param("reason", "Ly do tu choi")
                .param("sourceMobile", "0395954111")
                .param("sourceNumber", "3")
                .param("identityCardType", "CMTND")
                .param("identityCardNumber", "132464")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Dữ liệu truyền vào không trùng khớp trong database")))
                .andExpect(jsonPath("$.code", is("ARPU400")));
    }

}
