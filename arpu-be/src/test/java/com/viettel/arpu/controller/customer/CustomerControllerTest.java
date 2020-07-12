/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */

package com.viettel.arpu.controller.customer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test cho CustomerController
 *
 * @author trungnb3
 * @Date :6/8/2020, Mon
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testItShouldReturnBadRequestInResponseWhenNoLockStatus() throws Exception {
        this.mockMvc.perform(get("/api/customer/search?activeStatus=all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("yêu cầu không hợp lệ.")));
    }

    @Test
    void testItShouldReturnOKInResponseWhenBothActiveStatusAndLockStatusIsAll() throws Exception {
        this.mockMvc.perform(get("/api/customer/search?activeStatus=all&lockStatus=all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Thành công.")));
    }

    @Test
    void testItShouldReturnOKAndHave1CustomersInResponseWhenmsisdnWasSpecifiedAndBothActiveStatusAndLockStatusIsAll() throws Exception {
        this.mockMvc.perform(get("/api/customer/search?activeStatus=all&lockStatus=all&msisdn=0356447841"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content.length()", is(1)))
                .andExpect(jsonPath("$.code", is("ARPU200")));
    }

    @Test
    void testItShouldReturnOKAndHave1CustomersInResponseWhenActiveStatusIsActiveAndLockStatusIsAll() throws Exception {
        this.mockMvc.perform(get("/api/customer/search?activeStatus=active&lockStatus=all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content[0].activeStatus", is("Đang hoạt động")))
                .andExpect(jsonPath("$.code", is("ARPU200")));
    }


    @Test
    void testItShouldReturnOKAndHave10CustomersInResponseWhenActiveStatusIsInActiveAndLockStatusIsAll() throws Exception {
        this.mockMvc.perform(get("/api/customer/search?activeStatus=inactive&lockStatus=all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content[0].activeStatus", is("Không hoạt động")))
                .andExpect(jsonPath("$.code", is("ARPU200")));
    }

    @Test
    void testItShouldReturnOKAndHave5CustomersInResponseWhenActiveStatusIsAllAndLockStatusIsLock() throws Exception {
        this.mockMvc.perform(get("/api/customer/search?activeStatus=all&lockStatus=lock"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content[0].lockStatus", is("Khóa cho vay")))
                .andExpect(jsonPath("$.code", is("ARPU200")));
    }

    @Test
    void testItShouldReturnOKAndHave5CustomersInResponseWhenActiveStatusIsAllAndLockStatusIsUnLock() throws Exception {
        this.mockMvc.perform(get("/api/customer/search?activeStatus=all&lockStatus=unlock"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content[0].lockStatus", is("Mở khóa cho vay")))
                .andExpect(jsonPath("$.code", is("ARPU200")));
    }

    @Test
    void testItShouldReturnCustomerNotFound() throws Exception {
        this.mockMvc.perform(post("/api/customer/lock/1000"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.code", is("ARPU404")));
    }

    @Test
    void testItShouldLockCustomerWhenCustomerIsInUnLockStatus() throws Exception {
        this.mockMvc.perform(post("/api/customer/lock/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.code", is("ARPU200")));

        this.mockMvc.perform(post("/api/customer/lock/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.code", is("ARPU407")))
                .andExpect(jsonPath("$.message", is("Tài khoản này đã được khóa trước đó rồi.")));

    }

    @Test
    void testItShouldUnLockCustomerWhenCustomerIsInLockStatus() throws Exception {
        this.mockMvc.perform(post("/api/customer/unlock/12"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.code", is("ARPU200")));

        this.mockMvc.perform(post("/api/customer/unlock/12"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.code", is("ARPU408")))
                .andExpect(jsonPath("$.message", is("Tài khoản này đã được mở khóa trước đó rồi.")));

        this.mockMvc.perform(post("/api/customer/unlock/14"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.code", is("ARPU408")))
                .andExpect(jsonPath("$.message", is("Tài khoản này đã được mở khóa trước đó rồi.")));
    }

}



