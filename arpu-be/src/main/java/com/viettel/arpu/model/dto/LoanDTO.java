/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Author VuHQ
 * @Since 5/25/2020
 */
@Getter
@Setter
@ToString
public class LoanDTO {
    @JsonProperty("loanId")
    private String id = "";
    private String fullName = "";
    @JsonProperty("phone")
    private String msisdn = "";
    @JsonProperty("gender")
    private String gender = "";
    @JsonProperty("identityNumber")
    private String identityNumber = "";
    @JsonProperty("identityType")
    private String identityType = "";
    @JsonProperty("dateOfIssue")
    private LocalDate dateOfIssue = null;
    @JsonProperty("placeOfIssue")
    private String placeOfIssue = "";
    @JsonProperty("minLimit")
    private String minimumLimit = "";
    @JsonProperty("maxLimit")
    private String maximumLimit = "";
    @JsonProperty("loanStatus")
    private String loanStatus = "";
    @JsonProperty("loanAmount")
    private String loanAmount = "";
    @JsonProperty("loanDate")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDateTime createdDate = null;
    @JsonProperty("loanTerm")
    private String loanTerm = "";
    @JsonProperty("approveStatus")
    private String approveStatus = "";
    @JsonProperty("interestRate")
    private String interestRate = "";
    private String repaymentForm = "";
    private String currentDebtBalance = "";
    private String minAmountPaid = "";
    private String loanAccount = "";
    private String limitRemaining = "";
    private String contractLink = "";
    private String amountSpent = "";
}
