/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.model.request.mb;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @Author VuHQ
 * @Since 6/18/2020
 */
@Getter
@Setter
@ToString
public class MbGetLimitLoanForm {
    @NotNull
    private Long id = null;
}
