/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.model.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
public class CustomerDTO {
    private Long id;
    private String msisdn;
    private String fullName;
    private String email;
    private String nationality;
    private LocalDate dateOfBirth;
    private String gender;
    private String identityType;
    private String identityNumber;
    private LocalDate dateOfIssue;
    private String placeOfIssue;
    private Double loanMinimum;
    private Double loanMaximum;
    private Long arpu;
    private Integer scoreMin;
    private Integer scoreMax;
    private String lockStatus;
    private String lockStatusId;
    private String activeStatus;

    private String addressDetail;
    private String district;
    private String province;
    private String village;
    private String permanentAddress;
    private String viettelpayWallet;

}
