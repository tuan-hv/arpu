/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */

package com.viettel.arpu.controller.mb;

import com.viettel.arpu.model.request.LoanApprovalForm;
import com.viettel.arpu.model.response.mb.MBKYCResponse;
import com.viettel.arpu.service.mb.MBService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CallMbTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MBService mbService;

    @Test
    void testReturnSuccess() throws Exception {

        MBKYCResponse mbkycResponse = new MBKYCResponse();

        mbkycResponse.setErrorCode("00");
        mbkycResponse.setErrorDesc("00");

        Mockito.when(mbService.updateLoanInfo(any(LoanApprovalForm.class))).thenReturn(mbkycResponse);

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
                .andExpect(jsonPath("$.message", is("Thành công.")))
                .andExpect(jsonPath("$.code", is("ARPU200")));

    }

    @Test
    void testReturnFalseWhenCallAPIMB() throws Exception {

        MBKYCResponse mbkycResponse = new MBKYCResponse();

        mbkycResponse.setErrorCode("11");
        mbkycResponse.setErrorDesc("11");

        Mockito.when(mbService.updateLoanInfo(any(LoanApprovalForm.class))).thenReturn(mbkycResponse);

        this.mockMvc.perform(put("/api/mb/loans/loan_approval")
                .param("loanId", "10")
                .param("codeId", "PDS_03")
                .param("reason", "Ly do tu choi")
                .param("sourceMobile", "0395954111")
                .param("sourceNumber", "3")
                .param("identityCardType", "CMTND")
                .param("identityCardNumber", "24354545")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Thành công.")))
                .andExpect(jsonPath("$.code", is("ARPU200")));

    }
}
