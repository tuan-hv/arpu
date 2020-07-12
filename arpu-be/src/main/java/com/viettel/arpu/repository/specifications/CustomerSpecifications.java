/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.repository.specifications;

import com.viettel.arpu.model.entity.Customer;
import com.viettel.arpu.model.entity.Customer_;
import com.viettel.arpu.model.entity.Version_;
import com.viettel.arpu.model.request.CustomerSearchForm;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * @author trungnb3
 * @Date :6/9/2020, Tue
 */
public class CustomerSpecifications {
    private final List<Specification<Customer>> specifications = new ArrayList<>();

    public static CustomerSpecifications spec() {
        return new CustomerSpecifications();
    }

    public CustomerSpecifications msisdn(String msisdn) {
        if (!Strings.isEmpty(msisdn)) {
            specifications.add((Root<Customer> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                    criteriaBuilder.equal(root.get(Customer_.msisdn), msisdn.trim()));
        }
        return this;
    }

    public CustomerSpecifications active(Long versionLatest) {
        specifications.add((Root<Customer> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.join(Customer_.VERSION).get(Version_.VERSION_SYNC), versionLatest));
        return this;
    }

    public CustomerSpecifications inactive(Long versionLatest) {
        specifications.add((Root<Customer> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.lessThan(root.join(Customer_.VERSION).get(Version_.VERSION_SYNC), versionLatest));
        return this;
    }

    public CustomerSpecifications lock(CustomerSearchForm.LOCK_REQUEST lockRequest) {
        if (lockRequest != CustomerSearchForm.LOCK_REQUEST.ALL) {
            specifications.add((Root<Customer> root, CriteriaQuery<?> criteriaQuery,
                                CriteriaBuilder criteriaBuilder) ->
                    lockRequest == CustomerSearchForm.LOCK_REQUEST.LOCK ?
                            criteriaBuilder.equal(root.get(Customer_.lockStatus), Customer.LOCK_STATUS.LOCK)
                            : criteriaBuilder.or(criteriaBuilder.isNull(root.get(Customer_.lockStatus)),
                                criteriaBuilder.equal(root.get(Customer_.lockStatus), Customer.LOCK_STATUS.UNLOCK)));
        }
        return this;
    }

    public Specification<Customer> build() {
        return specifications.stream().reduce(allSpec(), Specification::and);
    }

    /**
     * @description khi không thỏa mãn đầu vào thì trả về search tất cả
     * @return specification
     */
    private Specification<Customer> allSpec() {
        return (Root<Customer> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> cb.equal(cb.literal(1), 1);
    }

}
