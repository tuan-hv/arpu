/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.controller.mobile;

import com.viettel.arpu.model.dto.WithdrawMoneyDTO;
import com.viettel.arpu.model.dto.mb.MbCustomerDTO;
import com.viettel.arpu.model.dto.mb.MbExistPhoneNumberDTO;
import com.viettel.arpu.model.dto.mb.MbListLoanDTO;
import com.viettel.arpu.model.dto.mb.MbLoanLimitDTO;
import com.viettel.arpu.model.request.WithdrawMoneyForm;
import com.viettel.arpu.model.request.mb.MbCheckCustomerForm;
import com.viettel.arpu.model.request.mb.MbGetLimitLoanForm;
import com.viettel.arpu.model.response.BaseResponse;
import com.viettel.arpu.service.loan.MobileService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Author VuHQ
 * @Since 6/11/2020
 */
@RestController
@RequestMapping("/api/mb/loans")
public class MobileLoanController {
    @Autowired
    private MobileService mobileService;

    /**
     * @description rút tiền từ MB vào VTP
     * @param withdrawMoneyForm
     * @return
     */
    @ApiOperation(value = "Rút tiền từ ngân hàng về ví",
            notes = "<b>Gửi bản tin iso để rút tiền</b>")
    @PostMapping("/withraw")
    public ResponseEntity<BaseResponse<WithdrawMoneyDTO>> withdrawMoney(
            @RequestBody @Valid WithdrawMoneyForm withdrawMoneyForm) {
        return ResponseEntity.ok(
                new BaseResponse<>(mobileService.withdrawMoneyToVTP(withdrawMoneyForm)));
    }

    /**
     * @description api lấy ra toàn bộ khoản vay của khách hàng, trong đó có trạng thái vay của khoản vay cuối cùng
     * và tổng số khoản vay của khách hàng
     * @param mbCheckCustomerForm
     * @return
     */
    @GetMapping
    public ResponseEntity<BaseResponse<MbListLoanDTO>> getAllLoan(@Valid MbCheckCustomerForm mbCheckCustomerForm){
        return ResponseEntity.ok(
                new BaseResponse<>(mobileService.getLoansByPhone(mbCheckCustomerForm)));
    }

    /**
     * @description kiểm tra xem khách hàng có tồn tại trong while list không
     * @param mbCheckCustomerForm
     * @return
     */
    @GetMapping("/customers/exist")
    public ResponseEntity<BaseResponse<MbExistPhoneNumberDTO>> isExistCustomer(
            @Valid MbCheckCustomerForm mbCheckCustomerForm){
        return ResponseEntity.ok(
                new BaseResponse<>(mobileService.isExistCustomer(mbCheckCustomerForm))
        );
    }

    @GetMapping("customers/reference")
    public ResponseEntity<BaseResponse<MbCustomerDTO>> getRelationshipCustomer(
            @Valid MbCheckCustomerForm mbCheckCustomerForm) {
        return ResponseEntity.ok(
                new BaseResponse<>(mobileService.getCustomer(mbCheckCustomerForm)));
    }
}
