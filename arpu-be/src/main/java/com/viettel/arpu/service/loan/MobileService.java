/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.service.loan;

import com.viettel.arpu.model.dto.WithdrawMoneyDTO;
import com.viettel.arpu.model.dto.mb.MbCustomerDTO;
import com.viettel.arpu.model.dto.mb.MbExistPhoneNumberDTO;
import com.viettel.arpu.model.dto.mb.MbListLoanDTO;
import com.viettel.arpu.model.dto.mb.MbLoanLimitDTO;
import com.viettel.arpu.model.request.WithdrawMoneyForm;
import com.viettel.arpu.model.request.mb.MbCheckCustomerForm;

/**
 * @Author VuHQ
 * @Since 6/5/2020
 */
public interface MobileService {
    MbExistPhoneNumberDTO isExistCustomer(MbCheckCustomerForm mbCheckCustomerForm);
    MbListLoanDTO getLoansByPhone(MbCheckCustomerForm mbCheckCustomerForm);
    MbCustomerDTO getCustomer(MbCheckCustomerForm mbCheckCustomerForm);
    WithdrawMoneyDTO withdrawMoneyToVTP(WithdrawMoneyForm withdrawMoneyForm);
    MbLoanLimitDTO getCustomerLimit(MbCheckCustomerForm mbCheckCustomerForm);
}
