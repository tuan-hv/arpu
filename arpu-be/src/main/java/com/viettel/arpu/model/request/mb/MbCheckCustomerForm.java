/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.model.request.mb;

import com.viettel.arpu.validator.Phone;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * @Author VuHQ
 * @Since 5/29/2020
 */
@Getter
@Setter
public class MbCheckCustomerForm {
    @Phone
    @NotBlank
    private String phoneNumber = "";
}
