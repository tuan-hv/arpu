/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.service.mb;

import com.viettel.arpu.model.dto.mb.MbLoanLimitDTO;
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
import com.viettel.arpu.model.response.mb.MbBaseResponse;
import com.viettel.arpu.model.response.mb.MbHistoriesLoanResponse;

public interface MbRegisterLoanService {


    MbBaseResponse sendToMBApprove(MbLoanRegistrationForm mbLoanRegistrationForm);


    MbHistoriesLoanResponse getHistories(MbHistoriesLoanForm mbHistoriesLoanForm);


    MbBaseResponse confirmLimit(MbConfirmLimitForm mbConfirmLimitForm);


    MbBaseResponse checkLoanLimit(MbChangeLimitForm mbChangeLimitForm, Long id);


    MbBaseResponse checkPayLoan(MbPayLoanForm mbPayLoanForm);


    MbBaseResponse confirmPayLoan(MbConfirmLimitForm mbConfirmLimitForm);


    MbLoanLimitDTO getLoanLimitInMB(MbGetLimitForm mbGetLimitForm);

    MbBaseResponse checkFinalLoan(MbFinalLoanForm mbPayLoanForm);

    MbBaseResponse confirmFinalLoan(MbConfirmLimitForm mbConfirmLimitForm);

    MbBaseResponse confirmCreateLoan(MbConfirmCreateLoanForm mbConfirmLoanForm);

    MbBaseResponse validatePin(MbPinForm mbPinForm);

    MbBaseResponse validateOtp(MbOtpForm mbPinForm);

    MbBaseResponse verifyOtp(MbVerifyOtpForm mbVerifyOtpForm);

}
