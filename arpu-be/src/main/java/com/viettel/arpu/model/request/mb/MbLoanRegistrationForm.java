/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.model.request.mb;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.viettel.arpu.constant.enums.PayType;
import com.viettel.arpu.validator.IdentityType;
import com.viettel.arpu.validator.Phone;
import com.viettel.arpu.validator.Reference;
import com.viettel.arpu.validator.Term;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author VuHQ
 * @Since 5/29/2020
 */
@Getter
@Setter
@Reference(referencerType = "referencerType", referencerEmail = "referencerEmail",
           referencerMobile = "referencerMobile", referencerName = "referencerName")
public class MbLoanRegistrationForm extends MbRequestIdForm{
    public static  Map<String, Object> requestStore = new ConcurrentHashMap<>();
    @NotBlank
    @Phone
    private String sourceMobile = "";
    @NotBlank
    private String sourceNumber = "";
    private String email = "";
    @NotNull
    @Past
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthday = null;
    @NotBlank
    @Range(min = 1, max = 2)
    private String gender = "";
    @NotBlank
    private String nationality = "";
    @IdentityType
    private String identityCardType = "";
    @NotBlank
    private String identityCardNumber = "";
    @NotNull
    @Past
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate issueDate = null;
    @NotBlank
    private String issuePlace = "";
    @NotBlank
    @Term
    private String term = "";
    @NotNull
    private PayType payType = null;
    @NotBlank
    @Range(min = 1, max = 2)
    private String loanType = "";
    @NotNull
    private BigDecimal amount = null;
    @NotNull
    private BigDecimal fee = null;
    @NotNull
    @Range(min = 0, max = 1)
    private byte isAutomaticPayment = 0;
    private String referencerName = "";
    private String referencerType = "";
    @Phone
    private String referencerMobile = "";
    @Email
    private String referencerEmail = "";
    private String village = "";
    private String district = "";
    private String province = "";
    private String addressDetail = "";
    private String permanentAddress = "";
}
