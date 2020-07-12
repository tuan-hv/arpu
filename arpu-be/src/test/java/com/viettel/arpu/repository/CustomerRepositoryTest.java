/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */

package com.viettel.arpu.repository;

import com.viettel.arpu.model.entity.Customer;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.BDDAssertions.then;

@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional
class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;


    @Test()
    void testFindByMsisdn() {
        Customer customer = customerRepository.findByMsisdn("0356447841").orElse(null);
        then(customer).isNotNull();
        then(customer.getMsisdn()).isEqualTo("0356447841");
    }

    @Test
    void testCustomerValid2Lock() {
        //Customer has loan status KVS_05
        Customer customer = customerRepository.customerValid2Lock(12L);
        then(customer).isNotNull();
        then(customer.getLoans().size()).isEqualTo(1);
        then(customer.getLoans().iterator().next().getLoanStatus().getId()).isEqualTo("KVS_05");

        //Customer has loan status KVS_04
        customer = customerRepository.customerValid2Lock(2L);
        then(customer).isNotNull();
        then(customer.getLoans().size()).isEqualTo(1);
        then(customer.getLoans().iterator().next().getLoanStatus().getId()).isEqualTo("KVS_04");

        //Customer has loan status KVS_01
        customer = customerRepository.customerValid2Lock(9L);
        then(customer).isNotNull();
        then(customer.getLoans().size()).isEqualTo(1);
        then(customer.getLoans().iterator().next().getLoanStatus().getId()).isEqualTo("KVS_01");

        //Customer has loan status not in (KVS_05, KVS_01, KVS_04)
        customer = customerRepository.customerValid2Lock(1L);
        then(customer).isNull();
    }

    @Test
    void testCustomerHasNoLoan() {
        Customer customer = customerRepository.customerHasNoLoan(6L);
        then(customer).isNull();
    }

    @Test
    void testGetCountCustomer() {
        Long customer = customerRepository.getCountCustomer("0356447841", "145542148");
        then(customer).isEqualTo(1l);
    }
}
