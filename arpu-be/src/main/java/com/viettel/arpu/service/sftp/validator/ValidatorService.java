/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.service.sftp.validator;

import com.viettel.arpu.model.entity.Customer;
import com.viettel.arpu.model.entity.Version;

import java.util.List;

/**
 * Interface validate data đồng bộ whitelist và tất toán khoản vay
 */
public interface ValidatorService {
    List<Customer> validateWhiteListData(List<String> list, Version version);

    List<String> validatePayLoanList(List<String> list, Version version);
}
