/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.viettel.arpu.constant.CommonConstant;
import com.viettel.arpu.locale.Translator;
import com.viettel.arpu.model.dto.LoanDTO;
import com.viettel.arpu.utils.ObjectMapperUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Entity(name = "Loan")
@Table(name = "tbl_loan")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class Loan extends AbstractAuditingEntity {
    private static final long serialVersionUID = 4934579490122693039L;
    /**
     * Id khoản vay
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    /**
     * Lãi suất khoản vay
     */
    @ManyToOne(cascade = CascadeType.ALL)
    private Interest interest;
    /**
     * Thông tin người vay
     */
    @ManyToOne
    @JsonIgnore
    private Customer customer;

    /**
     * ARPU 3 tháng gần nhất
     */
    private BigDecimal arpuLatestThreeMonths;

    /**
     * Số tiền vay
     */
    private BigDecimal loanAmount;

    /**
     * Hình thức trả nợ
     */
    private String repaymentForm;

    /**
     * Giới hạn nhỏ nhất
     */
    private BigDecimal minimumLimit;

    /**
     * Giới hạn lớn nhất
     */
    private BigDecimal maximumLimit;

    /**
     * Trạng thái duyệt
     */
    @ManyToOne
    private CodeCode approvalStatus;

    /**
     * Trạng thái khoản vay
     */
    @ManyToOne
    private CodeCode loanStatus;

    /**
     * Thông tin người đối chiếu
     */
    @OneToOne(cascade = CascadeType.ALL)
    private CustomerRef customerRef;

    /**
     * Phí dịch vụ
     */
    private BigDecimal fee;

    /**
     * Tên file hợp đồng
     */
    private String contractLink;

    /**
     * tài khoản vay
     */
    private String loanAccount;
    /**
     * Số tiền đã chi tiêu
     */
    private BigDecimal amountSpent;
    /**
     * Giới hạn còn lại
     */
    private BigDecimal limitRemaining;
    /**
     * Tiền lãi
     */
    private BigDecimal profitAmount;
    /**
     * Số tiền phải trả
     */
    private BigDecimal amountPay;
    /**
     * Ngày hết hạn
     */
    private LocalDate expirationDate;
    /**
     * Lý do từ chối
     */
    private String reasonRejection;
    /**
     * Tự động thanh toán
     */
    private byte isAutomaticPayment;

    public LoanDTO toLoanDTO() {
        LoanDTO loanDTO = ObjectMapperUtils.map(this, LoanDTO.class);
        Customer customerData = getCustomer();
        BeanUtils.copyProperties(customerData, loanDTO, "id", "createdDate");

        loanDTO.setGender(Translator.toLocale(customerData.getGender().toString()));
        loanDTO.setId(String.valueOf(getId()));

        loanDTO.setMaximumLimit(Optional.ofNullable(getMaximumLimit())
                .map(bigDecimal -> String.valueOf(bigDecimal.intValue()))
                .orElse(CommonConstant.DEFAULT_LIMIT));
        loanDTO.setMinimumLimit(Optional.ofNullable(getMaximumLimit())
                .map(bigDecimal -> String.valueOf(bigDecimal.intValue()))
                .orElse(CommonConstant.DEFAULT_LIMIT));
        loanDTO.setLoanAmount(Optional.ofNullable(getLoanAmount())
                .map(bigDecimal -> String.valueOf(bigDecimal.intValue()))
                .orElse(CommonConstant.DEFAULT_LIMIT));

        loanDTO.setLoanStatus(Optional.ofNullable(getLoanStatus())
                                            .flatMap(codeCode -> Optional.of(codeCode.getId()))
                                            .orElse(""));
        loanDTO.setApproveStatus(Optional.ofNullable(getApprovalStatus())
                                            .flatMap(codeCode -> Optional.of(codeCode.getId()))
                                            .orElse(""));

        Optional.ofNullable(getInterest()).ifPresent((Interest interest1) -> {
            loanDTO.setInterestRate(interest1.getInterestRate());
            loanDTO.setLoanTerm(interest1.getLoanTerm());
        });

        return loanDTO;
    }
}

