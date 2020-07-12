/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.model.dto.mb;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * @Author VuHQ
 * @Since 6/18/2020
 */
@Getter
@Setter
public class MbCustomerInfoDTO {
    private String msisdn;
    private String fullName;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateOfBirth;
    private String gender;
    private String identityType;
    private String identityNumber;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateOfIssue;
    private String placeOfIssue;
    private String email;
    private String permanentAddress;
    private String addressDetail;
    private String district;
    private String province;
    private String village;
    private String nationality;
}
