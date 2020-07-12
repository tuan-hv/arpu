/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.model.dto.mb;

import com.viettel.arpu.model.response.mb.MbBaseResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @author DoDV
 * @Date :6/10/2020, Wed
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MbConfirmCreateLoan {
    private String sourceMobile;
    private String sourceNumber;
    private String identityCardType;
    private String identityCardNumber;
    private String kyc;
}
