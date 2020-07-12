/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.model.request.mb;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.viettel.arpu.validator.IdentityType;
import com.viettel.arpu.validator.Phone;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @Author VuHQ
 * @Since 6/1/2020
 */
@Getter
@Setter
@ToString
public class MbChangeLimitForm extends MbRequestIdForm {
    @NotNull
    private Long id = 0L;
    @NotBlank
    @Phone
    private String sourceMobile = "";
    @NotBlank
    private String sourceNumber = "";
    @NotBlank
    private String loanAccount = "";
    @IdentityType
    private String identityCardType = "";
    @NotBlank
    private String identityCardNumber = "";
    @NotNull
    private BigDecimal changeAmount = null;
    @JsonIgnore
    private boolean isIncrease = true;

}
