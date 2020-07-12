/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.repository.specifications;

import com.viettel.arpu.model.entity.AbstractAuditingEntity_;
import com.viettel.arpu.model.entity.Customer;
import com.viettel.arpu.model.entity.Customer_;
import com.viettel.arpu.model.entity.Loan;
import com.viettel.arpu.model.entity.Loan_;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

/**
 * @Author VuHQ
 * @Since 5/29/2020
 */
public class MbSpecifications {

    private MbSpecifications() {
    }

    public static Specification<Loan> hasPhoneNumber(String phoneNumber) {
        Specification<Loan> specification = null;
        if (StringUtils.isEmpty(phoneNumber)) {
            specification = null;
        }
        else {
            specification = ((Root<Loan> root, CriteriaQuery<?> query,
              CriteriaBuilder criteriaBuilder) -> {
                Join<Loan, Customer> itemNode = root.join(Loan_.customer);
                query.orderBy(criteriaBuilder.desc(root.get(Loan_.ID)));
                query.distinct(true);
                return criteriaBuilder.equal(itemNode.get(Customer_.MSISDN), phoneNumber.trim());
            });
        }
        return specification;
    }

}
