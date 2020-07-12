/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.repository.specifications;

import com.viettel.arpu.constant.enums.ApprovalStatus;
import com.viettel.arpu.model.entity.AbstractAuditingEntity_;
import com.viettel.arpu.model.entity.CodeCode;
import com.viettel.arpu.model.entity.CodeCode_;
import com.viettel.arpu.model.entity.Customer;
import com.viettel.arpu.model.entity.Customer_;
import com.viettel.arpu.model.entity.Loan;
import com.viettel.arpu.model.entity.Loan_;
import com.viettel.arpu.utils.SpecificationUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author VuHQ
 * @Since 5/25/2020
 */

public class LoanSpecifications {
    private final List<Specification<Loan>> loanSpecs = new ArrayList<>();

    public static LoanSpecifications spec() {
        return new LoanSpecifications();
    }

    /**
     * @description add thêm điều kiện tìm kiếm ngày tạo khoản vay
     * @param fromDate
     * @return
     */
    public LoanSpecifications fromDate(LocalDate fromDate) {
        loanSpecs.add(SpecificationUtils
                      .greaterThanOrEqualFromDateInstant(fromDate, AbstractAuditingEntity_.CREATED_DATE));
        return this;
    }


    /**
     * @description add thêm điều kiện tìm kiếm ngày tạo khoản vay
     * @param toDate
     * @return
     */
    public LoanSpecifications toDate(LocalDate toDate) {
        loanSpecs.add(SpecificationUtils.lessThanOrEqualToDateInstant(toDate, AbstractAuditingEntity_.CREATED_DATE));
        return this;
    }

    /**
     * @description add thêm điều kiện tìm kiếm số điện thoại
     * @param phoneNumber
     * @return
     */
    public LoanSpecifications phoneNumber(String phoneNumber) {
        loanSpecs.add(hasPhoneNumber(phoneNumber));
        return this;
    }

    /**
     * @description add thêm điều kiện phê duyệt với số cmtnd/cccd
     * @param idCard
     * @return
     */
    public LoanSpecifications idCard(String idCard) {
        loanSpecs.add(hasIdCard(idCard));
        return this;
    }

    /**
     * @description add thêm điều kiện tìm kiếm với trạng thái khoản vay
     * @param loanStatus
     * @return
     */
    public LoanSpecifications loanStatus(String loanStatus) {
        loanSpecs.add(hasLoanStatus(loanStatus));
        return this;
    }

    /**
     * @description add thêm điều kiện tìm kiếm với trạng thái phê duyệt
     * @param approveStatus
     * @return
     */
    public LoanSpecifications approveStatus(String approveStatus) {
        loanSpecs.add(hasApproveStatus(approveStatus));
        return this;
    }

    /**
     * @description add thêm điều kiện tìm kiếm với trạng thái phê duyệt
     * @param approveStatus
     * @return
     */
    public LoanSpecifications approveStatusForMB(String approveStatus) {
        loanSpecs.add(hasApproveStatusForMB(approveStatus));
        return this;
    }

    /**
     * @description tạo ra câu lệnh where
     * @return specification
     */
    public Specification<Loan> build() {
        Specification<Loan> sp = loanSpecs.stream().reduce(all(), Specification::and);
        return Specification.where(sp);
    }

    /**
     * @description tạo ra câu lệnh so sánh số điện thoại trong database
     * @param phoneNumber
     * @return specification
     */
    public Specification<Loan> hasPhoneNumber(String phoneNumber) {
        return StringUtils.isEmpty(phoneNumber) ? all() : ((Root<Loan> root, CriteriaQuery<?> query,
                                                           CriteriaBuilder criteriaBuilder) -> {
            Join<Loan, Customer> itemNode = root.join(Loan_.customer);
            query.distinct(true);
            return criteriaBuilder.equal(itemNode.get(Customer_.MSISDN), phoneNumber.trim());
        });
    }

    /**
     * @description tạo ra câu lệnh so sánh số cmtnd/cccd trong database
     * @param idCard
     * @return specification
     */
    public Specification<Loan> hasIdCard(String idCard) {
        return StringUtils.isEmpty(idCard) ? all() :
                ((Root<Loan> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Join<Loan, Customer> itemNode = root.join(Loan_.customer);
            query.distinct(true);
            return criteriaBuilder.equal(itemNode.get(Customer_.IDENTITY_NUMBER), idCard.trim());
        });
    }

    /**
     * @description tạo ra câu lệnh so sánh trạng thái phê duyệt trong database
     * @param approveStatus
     * @return specification
     */
    public Specification<Loan> hasApproveStatus(String approveStatus) {
        return StringUtils.isEmpty(approveStatus) ||
                ApprovalStatus.STATUS_ALL.getStatus().equalsIgnoreCase(approveStatus.trim())
                ? all() : ((Root<Loan> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Join<Loan, CodeCode> itemNode = root.join(Loan_.approvalStatus);
            query.distinct(true);
            return criteriaBuilder.equal(itemNode.get(CodeCode_.ID), approveStatus.trim());
        });
    }

    /**
     * @description tạo ra câu lệnh so sánh trạng thái phê duyệt trong database
     * @param approveStatus
     * @return specification
     */
    public Specification<Loan> hasApproveStatusForMB(String approveStatus) {
        return StringUtils.isEmpty(approveStatus)
                ? all() : ((Root<Loan> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Join<Loan, CodeCode> itemNode = root.join(Loan_.approvalStatus);
            query.distinct(true);
            return criteriaBuilder.equal(itemNode.get(CodeCode_.ID), approveStatus.trim());
        });
    }

    /**
     * @Description tạo ra câu lệnh so sánh trạng thái khoản vay trong database
     * @param loanStatus
     * @return specification
     */
    public Specification<Loan> hasLoanStatus(String loanStatus) {
        return StringUtils.isEmpty(loanStatus) ||
                ApprovalStatus.STATUS_ALL.getStatus().equalsIgnoreCase(loanStatus.trim())
                ? all() : ((Root<Loan> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Join<Loan, CodeCode> itemNode = root.join(Loan_.loanStatus);
            query.distinct(true);
            return criteriaBuilder.equal(itemNode.get(CodeCode_.ID), loanStatus.trim());
        });
    }

    /**
     * @description tạo ra câu lệnh tìm kiếm trạng thái phê duyệt phù hợp
     * với list trạng thái phê duyệt cho trước
     * @param listApproveStatus
     * @return specification
     */
    public Specification<Loan> hasListApproveStatus(List<String> listApproveStatus) {
        return (Root<Loan> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Join<Loan, CodeCode> itemNode = root.join(Loan_.approvalStatus);
            query.distinct(true);
            return itemNode.get(CodeCode_.ID).in(listApproveStatus);
        };
    }

    /**
     * @description khi không thỏa mãn đầu vào thì trả về search tất cả
     * @return specification
     */
    public Specification<Loan> all() {
        return (Root<Loan> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> cb.equal(cb.literal(1), 1);
    }

}
