/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.service.sftp.form;

import com.viettel.arpu.constant.BatchConstant;
import com.viettel.arpu.model.entity.Address;
import com.viettel.arpu.model.entity.Customer;
import com.viettel.arpu.constant.enums.Gender;
import com.viettel.arpu.validator.IdentityType;
import com.viettel.arpu.validator.Numeric;
import com.viettel.arpu.validator.PhoneSyncWhiteList;
import com.viettel.arpu.validator.ScoreMatch;
import com.viettel.arpu.validator.Sex;
import com.viettel.arpu.validator.SyncDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

/**
 * Một class để valid các trường của một bản ghi, trong file đồng bộ whitelist
 *
 * @author tuongvx
 */
@Builder
@Getter
@AllArgsConstructor
@ScoreMatch(scoreMin = "scoreMin", scoreMax = "scoreMax", message = "error.msg.sync.whitelist.compare_score")
public class CustomerForm implements Serializable {

    private static final long serialVersionUID = 997755002642175142L;
    @PhoneSyncWhiteList(message = "error.msg.sync.phone")
    private String msisdn;
    @NotBlank(message = "error.msg.sync.whitelist.full_name.not_null")
    private String fullName;
    @SyncDate(message = "error.msg.sync.whitelist.date.of_birth")
    private String dateOfBirth;
    @Sex(message = "error.msg.sync.whitelist.gender")
    private String gender;
    @IdentityType(message = "error.msg.sync.whitelist.identity_type")
    private String identityType;
    @Numeric(message = "error.msg.sync.whitelist.identity_number")
    private String identityNumber;
    @SyncDate(message = "error.msg.sync.whitelist.date.of_issue")
    private String dateOfIssue;
    @NotBlank(message = "error.msg.sync.whitelist.place_of_issue.not_null")
    private String placeOfIssue;
    @Numeric(message = "error.msg.sync.whitelist.score_min")
    private String scoreMin;
    @Numeric(message = "error.msg.sync.whitelist.score_max")
    private String scoreMax;
    @Numeric(message = "error.msg.sync.whitelist.viettelpay_wallet")
    private String viettelpayWallet;
    @Numeric(message = "error.msg.sync.whitelist.arpu_lates_three_months")
    private String arpuLatestThreeMonths;

    public static Customer from(@Valid CustomerForm opt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Gender gender = Gender.FEMALE;
        if (Objects.equals(BatchConstant.MALE, opt.getGender().toUpperCase())) {
            gender = Gender.MALE;
        }
        return Optional.ofNullable(Customer.builder()
                .fullName(opt.getFullName())
                .msisdn(opt.getMsisdn())
                .dateOfBirth(LocalDate.parse(opt.getDateOfBirth(), formatter))
                .gender(gender)
                .identityType(opt.getIdentityType())
                .identityNumber(opt.getIdentityNumber())
                .dateOfIssue(LocalDate.parse(opt.getDateOfIssue(), formatter))
                .placeOfIssue(opt.getPlaceOfIssue())
                .scoreMin(Integer.valueOf(opt.getScoreMin()))
                .scoreMax(Integer.valueOf(opt.getScoreMax()))
                .viettelpayWallet(opt.viettelpayWallet)
                .arpuLatestThreeMonths(BigDecimal.valueOf(Long.parseLong(opt.getArpuLatestThreeMonths())))
                .address(new Address())
                .build()).orElse(new Customer());
    }

}
