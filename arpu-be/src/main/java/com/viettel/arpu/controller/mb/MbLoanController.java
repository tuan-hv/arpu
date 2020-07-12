/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.controller.mb;

import com.viettel.arpu.model.request.mb.MbChangeLimitForm;
import com.viettel.arpu.model.request.mb.MbConfirmCreateLoanForm;
import com.viettel.arpu.model.request.mb.MbConfirmLimitForm;
import com.viettel.arpu.model.request.mb.MbDetailLoanForm;
import com.viettel.arpu.model.request.mb.MbFinalLoanForm;
import com.viettel.arpu.model.request.mb.MbGetLimitForm;
import com.viettel.arpu.model.request.mb.MbHistoriesLoanForm;
import com.viettel.arpu.model.request.mb.MbLoanRegistrationForm;
import com.viettel.arpu.model.request.mb.MbOtpForm;
import com.viettel.arpu.model.request.mb.MbPayLoanForm;
import com.viettel.arpu.model.request.mb.MbPinForm;
import com.viettel.arpu.model.request.mb.MbVerifyOtpForm;
import com.viettel.arpu.model.response.BaseResponse;
import com.viettel.arpu.model.response.mb.MbBaseResponse;
import com.viettel.arpu.model.response.mb.MbHistoriesLoanResponse;
import com.viettel.arpu.service.mb.MbRegisterLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Author VuHQ
 * @Since 5/29/2020
 */
@RestController
@RequestMapping("/api/mb/loans")
public class MbLoanController {
    @Autowired
    private MbRegisterLoanService mbRegisterLoanService;

    /**
     * @description gửi hồ sơ sang cho MB phê duyệt
     * @param mbLoanRegistrationForm
     * @return
     */
    @PostMapping("/approval")
    public ResponseEntity<BaseResponse<MbBaseResponse>> sendToMBApproval(@RequestBody @Valid MbLoanRegistrationForm mbLoanRegistrationForm){
        return ResponseEntity.ok(new BaseResponse<>(mbRegisterLoanService.sendToMBApprove(mbLoanRegistrationForm)));
    }

    /**
     * @description gửi xác nhận hồ sơ sang cho MB phê duyệt
     * @param mbConfirmLoanForm
     * @return
     */
    @PostMapping("/confirms")
    public ResponseEntity<BaseResponse<MbBaseResponse>> confirmCreateLoan(@RequestBody @Valid MbConfirmCreateLoanForm mbConfirmLoanForm){
        return ResponseEntity.ok(
                new BaseResponse<>(mbRegisterLoanService.confirmCreateLoan(mbConfirmLoanForm))
        );
    }

    /**
     * @description gửi yêu cầu lấy lịch sử 5 giao dịch gần nhất cho MB
     * @param mbHistoriesLoanForm
     * @return
     */
    @PostMapping("/histories")
    public ResponseEntity<BaseResponse<MbHistoriesLoanResponse>> getLoanHistoriesInMB(@RequestBody @Valid MbHistoriesLoanForm mbHistoriesLoanForm){
        return ResponseEntity.ok(new BaseResponse<>(mbRegisterLoanService.getHistories(mbHistoriesLoanForm)));
    }

    /**
     * @description lấy thông tin hạn mức của khoản vay từ MB khi tạo tài khoản vay
     * (lúc này khoản vay chưa tồn tại trong hệ thống để lấy về)
     * @param mbGetLimitForm
     * @return
     */
    @PostMapping("/customers/limit")
    public ResponseEntity<BaseResponse<MbBaseResponse>> getLoanLimitInMB(@RequestBody @Valid MbGetLimitForm mbGetLimitForm){
        return ResponseEntity.ok(new BaseResponse<>(
                mbRegisterLoanService.getLoanLimitInMB(mbGetLimitForm)));
    }

    /**
     * @description gửi yêu cầu kiểm tra thay đổi hạn mức sang cho MB
     * @param mbChangeLimitForm
     * @param mbChangeLimitForm
     * @return
     */
    @PostMapping("/limit")
    public ResponseEntity<BaseResponse<MbBaseResponse>> checkLoanLimitInMB(@RequestBody @Valid MbChangeLimitForm mbChangeLimitForm){
        return ResponseEntity.ok(new BaseResponse<>(mbRegisterLoanService.checkLoanLimit(mbChangeLimitForm, mbChangeLimitForm.getId())));
    }

    /**
     * @description gửi xác nhận tăng hạn mức(chỉ khi kiểm tra thay đổi hạn mức ok thì ms xác nhận)
     * @param mbConfirmLimitForm
     * @return
     */
    @PutMapping("/limit")
    public ResponseEntity<BaseResponse<MbBaseResponse>> confirmChangeLimitInMB(@RequestBody @Valid MbConfirmLimitForm mbConfirmLimitForm){
        return ResponseEntity.ok(new BaseResponse<>(mbRegisterLoanService.confirmLimit(mbConfirmLimitForm)));
    }

    /**
     * @descritpion gửi yêu cầu kiểm tra thanh toán sang cho MB
     * @param mbPayLoanForm
     * @param mbPayLoanForm
     * @return
     */
    @PostMapping("/pay")
    public ResponseEntity<BaseResponse<MbBaseResponse>> checkPayLoanInMB(@RequestBody @Valid MbPayLoanForm mbPayLoanForm){
        return ResponseEntity.ok(new BaseResponse<>(mbRegisterLoanService.checkPayLoan(mbPayLoanForm)));
    }

    /**
     * @description gửi xác nhận thanh toán sang cho MB(chỉ xác nhận khi kiểm tra thành công)
     * @param mbConfirmLimitForm
     * @return
     */
    @PutMapping("/pay")
    public ResponseEntity<BaseResponse<MbBaseResponse>> confirmPayLoanInMB(@RequestBody @Valid MbConfirmLimitForm mbConfirmLimitForm){
        return ResponseEntity.ok(new BaseResponse<>(mbRegisterLoanService.confirmPayLoan(mbConfirmLimitForm)));
    }

    /**
     * @description gửi yêu cầu kiểm tra tất toán sang cho MB
     * @param mbFinalLoanForm
     * @return
     */
    @PostMapping("/final")
    public ResponseEntity<BaseResponse<MbBaseResponse>> checkFinalLoanInMB(@RequestBody @Valid MbFinalLoanForm mbFinalLoanForm){
        return ResponseEntity.ok(new BaseResponse<>(mbRegisterLoanService.checkFinalLoan(mbFinalLoanForm)));
    }

    /**
     * @description gửi xác nhận tất toán sang cho MB(chỉ gửi khi kết quả kiểm tra thành công)
     * @param mbConfirmLimitForm
     * @return
     */
    @PutMapping("/final")
    public ResponseEntity<BaseResponse<MbBaseResponse>> confirmFinalLoanInMB(@RequestBody @Valid MbConfirmLimitForm mbConfirmLimitForm){
        return ResponseEntity.ok(new BaseResponse<>(mbRegisterLoanService.confirmFinalLoan(mbConfirmLimitForm)));
    }

    /**
     * @description kiểm tra mã pin
     * @param mbPinForm
     * @return
     */
    @PostMapping("/pin")
    public ResponseEntity<BaseResponse<MbBaseResponse>> validatePin(@RequestBody @Valid MbPinForm mbPinForm){
        return ResponseEntity.ok(new BaseResponse<>(mbRegisterLoanService.validatePin(mbPinForm)));
    }
    /**
     * @description sinh OTP
     * @param mbPinForm
     * @return
     */

    @PostMapping("/otp")
    public ResponseEntity<BaseResponse<MbBaseResponse>> createOTP(@RequestBody @Valid MbOtpForm mbPinForm){
        return ResponseEntity.ok(new BaseResponse<>(mbRegisterLoanService.validateOtp(mbPinForm)));
    }

    /**
     * @description xác nhận lại otp
     * @param mbVerifyOtpForm
     * @return
     */
    @PostMapping("/otp/verify")
    public ResponseEntity<BaseResponse<MbBaseResponse>> verifyOTP(@RequestBody @Valid MbVerifyOtpForm mbVerifyOtpForm){
        return ResponseEntity.ok(new BaseResponse<>(mbRegisterLoanService.verifyOtp(mbVerifyOtpForm)));
    }


}
