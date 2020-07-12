/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.model.request.mb;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @Author VuHQ
 * @Since 6/11/2020
 */
@Getter
@Setter
@ToString
public class MbSendToMbApprovalForm{
    private String sourceMobile = "";
    private String sourceNumber = "";
    private String identityCardType = "";
    private String identityCardNumber = "";
    private String loanType = "";
    private String email = "";
    @JsonFormat(pattern = "yyyyMMdd")
    private LocalDate birthday = null;
    private String gender = "";
    private String nationality = "";
    @JsonFormat(pattern = "yyyyMMdd")
    private LocalDate issueDate = null;
    private String issuePlace = "";
    private String term = "";
    private String payType = "";
    private String referencerName = "";
    private String referencerType = "";
    private String referencerMobile = "";
    private String referencerEmail = "";
    private BigDecimal amount = null;
    private BigDecimal fee = null;
    private Integer scoreMin = null;
    private Integer scoreMax = null;
    private BigDecimal scoreAve = null;
}
