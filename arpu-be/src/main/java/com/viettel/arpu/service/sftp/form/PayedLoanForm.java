/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.service.sftp.form;

import com.viettel.arpu.validator.Numeric;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

/**
 * Một class để valid các trường của một bản ghi, trong file đồng bộ tất toán khoản vay
 *
 * @author tuongvx
 */
public class PayedLoanForm {
    @NotNull
    @Numeric(message = "error.msg.sync.payLoan.account.number")
    private String numberAccount;
}
