/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */

package com.viettel.arpu.controller.loan;

import com.viettel.arpu.config.FtpStorageProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LoanDetailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    FtpStorageProperties ftpStorageProperties;

    @Test
    void testReturnSuccessWhenCallAPIGetLoanSuccess() throws Exception {

        this.mockMvc.perform(get("/api/loans/2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Thành công.")))
                .andExpect(jsonPath("$.code", is("ARPU200")))
                // Expect Loan Info
                .andExpect(jsonPath("$.data.loanInfo.id", is(2)))
                .andExpect(jsonPath("$.data.loanInfo.arpuLatestThreeMonths", is(700.00)))
                .andExpect(jsonPath("$.data.loanInfo.loanAmount", is(60000000.00)))
//                .andExpect(jsonPath("$.data.loanInfo.loanTerm", is("1 tháng")))
                .andExpect(jsonPath("$.data.loanInfo.repaymentForm", is("Thu goc cuoi ky")))
                .andExpect(jsonPath("$.data.loanInfo.minimumLimit", is(70000000.00)))
                .andExpect(jsonPath("$.data.loanInfo.maximumLimit", is(50000000.00)))
                .andExpect(jsonPath("$.data.loanInfo.amountSpent", nullValue()))
                .andExpect(jsonPath("$.data.loanInfo.limitRemaining", nullValue()))
                .andExpect(jsonPath("$.data.loanInfo.profitAmount", nullValue()))
                .andExpect(jsonPath("$.data.loanInfo.amountPay", nullValue()))
                .andExpect(jsonPath("$.data.loanInfo.expirationDate", nullValue()))
//                .andExpect(jsonPath("$.data.loanInfo.approvalStatus", is("Đã phê duyệt")))
//                .andExpect(jsonPath("$.data.loanInfo.loanStatus", is("Đã tất toán")))
                .andExpect(jsonPath("$.data.loanInfo.reasonRejection", is("aaa")))
                .andExpect(jsonPath("$.data.loanInfo.loanAccount", nullValue()))
                .andExpect(jsonPath("$.data.loanInfo.interestRate", is("10")))
                .andExpect(jsonPath("$.data.loanInfo.requestId", nullValue()))
                // Expect customerInfo
                .andExpect(jsonPath("$.data.customerInfo.id", is(2)))
                .andExpect(jsonPath("$.data.customerInfo.msisdn", is("0356447840")))
                .andExpect(jsonPath("$.data.customerInfo.fullName", is("HuongNT1")))
                .andExpect(jsonPath("$.data.customerInfo.email", is("Trangtt@gmail.com")))
                .andExpect(jsonPath("$.data.customerInfo.nationality", is("VN")))
                .andExpect(jsonPath("$.data.customerInfo.viettelpayWallet", is("1")))
                .andExpect(jsonPath("$.data.customerInfo.dateOfBirth", is("01/08/1993")))
                .andExpect(jsonPath("$.data.customerInfo.gender", is("Nam")))
                .andExpect(jsonPath("$.data.customerInfo.identityType", is("CMTND")))
                .andExpect(jsonPath("$.data.customerInfo.identityNumber", is("1451542148")))
                .andExpect(jsonPath("$.data.customerInfo.dateOfIssue", is("01/08/2007")))
                .andExpect(jsonPath("$.data.customerInfo.placeOfIssue", is("HN")))
                .andExpect(jsonPath("$.data.customerInfo.loanMinimum", is(7.0E7)))
                .andExpect(jsonPath("$.data.customerInfo.loanMaximum", is(5000000.0)))
                .andExpect(jsonPath("$.data.customerInfo.arpu", is(600)))
                .andExpect(jsonPath("$.data.customerInfo.scoreMin", is(600)))
                .andExpect(jsonPath("$.data.customerInfo.scoreMax", is(700)))
                .andExpect(jsonPath("$.data.customerInfo.lockStatus", is("LOCK")))
                .andExpect(jsonPath("$.data.customerInfo.lockStatusId", nullValue()))
                .andExpect(jsonPath("$.data.customerInfo.activeStatus", nullValue()))
                .andExpect(jsonPath("$.data.customerInfo.addressDetail", is("Doi Can, Ba Dinh, Ha Noi")))
                .andExpect(jsonPath("$.data.customerInfo.district", is("Ba Dinh")))
                .andExpect(jsonPath("$.data.customerInfo.province", is("Ha Noi")))
                .andExpect(jsonPath("$.data.customerInfo.village", is("Lang")))
                .andExpect(jsonPath("$.data.customerInfo.permanentAddress", nullValue()))
                // Expect referenceInfo
                .andExpect(jsonPath("$.data.referenceInfo.fullName", nullValue()))
                .andExpect(jsonPath("$.data.referenceInfo.relationship", nullValue()))
                .andExpect(jsonPath("$.data.referenceInfo.msisdn", nullValue()))
                .andExpect(jsonPath("$.data.referenceInfo.email", nullValue()))
                // Expect contractLink
                .andExpect(jsonPath("$.data.contractLink", nullValue()));

    }

    @Test
    void testReturnBadRequestWhenCallAPIGetLoanWithParamInvalid() throws Exception {
        this.mockMvc.perform(get("/api/loans/a"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("lỗi máy chủ")))
                .andExpect(jsonPath("$.code", is("ARPU500")));
    }

    @Test
    void testReturnNotFoundWhenGetLoanNotFound() throws Exception {
        this.mockMvc.perform(get("/api/loans/111"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Không tìm thấy khoản vay thỏa mãn.")))
                .andExpect(jsonPath("$.code", is("ARPU405")));
    }

    @Test
    void testReturnBadRequestWhenCallAPIMbInvalid() throws Exception {
        this.mockMvc.perform(post("/api/loans/update_mb_approvals"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("yêu cầu không hợp lệ.")))
                .andExpect(jsonPath("$.code", is("ARPU400")));
    }

    @Test
    void testReturnNotFoundWhenCallAPIMbInvalid() throws Exception {
        this.mockMvc.perform(put("/api/loans/update_mb_approvals"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("yêu cầu không hợp lệ.")))
                .andExpect(jsonPath("$.code", is("ARPU400")));
    }

    @Test
    void testReturnSuccessWhenCallApiUploadFile() throws Exception {

        ftpStorageProperties.setHost("192.90.90.54");
        ftpStorageProperties.setPort("22");
        ftpStorageProperties.setUser("newuser");
        ftpStorageProperties.setPwd("user@123");
        ftpStorageProperties.setUploadDir("/home/newuser/uploads/");

        byte[] array = Files.readAllBytes(Paths.get("src/test/resources/A.pdf"));

        MockMultipartFile firstFile = new MockMultipartFile("file", "A.pdf", "application/pdf", array);
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/loans/update_mb_approval")
                .file(firstFile)
                .param("id", "6"))
                .andExpect(status().isOk())

                .andExpect(content().string(containsString("Thành công.")))
                .andExpect(jsonPath("$.code", is("ARPU200")))
//                .andExpect(jsonPath("$.data.loanStatus", is("Chờ MB phê duyệt")))
        ;
    }

    @Test
    void testReturnBadRequestWhenCallApiUploadMissingFile() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/loans/update_mb_approval")
                .param("id", "4"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("File chưa đúng định dạng (.pdf)")))
                .andExpect(jsonPath("$.code", is("ARPU400")));
    }

    @Test
    void testReturnNotFoundWhenCallAPIMbMissingId() throws Exception {
        this.mockMvc.perform(post("/api/loans/update_mb_approval")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "2")
                .param("file", "file.pdf"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}
