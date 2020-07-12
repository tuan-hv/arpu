/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WithdrawMoneyDTO {
    private String sourceMobile;
    private String amount;
    private String bankAccount;
}
