/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.service.customer.impl;

import com.viettel.arpu.exception.CustomerHasBeenLockException;
import com.viettel.arpu.exception.CustomerHasBeenUnLockException;
import com.viettel.arpu.exception.CustomerLockFailException;
import com.viettel.arpu.exception.CustomerNotFoundException;
import com.viettel.arpu.model.entity.Address;
import com.viettel.arpu.model.entity.Customer;
import com.viettel.arpu.model.entity.Version;
import com.viettel.arpu.model.request.CustomerSearchForm;
import com.viettel.arpu.repository.CustomerRepository;
import com.viettel.arpu.repository.specifications.CustomerSpecifications;
import com.viettel.arpu.service.customer.CustomerService;
import com.viettel.arpu.utils.BatchStatus;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author trungnb3
 * @Date :5/21/2020, Thu
 */
@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * lấy tất  customer với activeStatus = active,inactive
     *
     * @param customerRequest The customer request
     * @param pageable        Request paging object
     * @return Page<Customer> customer paging object
     */
    public Page<Customer> all(@Valid CustomerSearchForm customerRequest, Pageable pageable) {
        return customerRepository.findAll(CustomerSpecifications
                .spec()
                .msisdn(customerRequest.getMsisdn())
                .lock(customerRequest.lockRequest())
                .build(), pageable);
    }

    /**
     * lấy tất  customer với activeStatus = active
     *
     * @param customerRequest The customer request
     * @param latestVersion   the latest version
     * @param pageable        Request paging object
     * @return Page<Customer> customer paging object
     */
    public Page<Customer> active(@Valid CustomerSearchForm customerRequest, Long latestVersion, Pageable pageable) {
        return customerRepository.findAll(CustomerSpecifications
                .spec()
                .msisdn(customerRequest.getMsisdn())
                .lock(customerRequest.lockRequest())
                .active(latestVersion)
                .build(), pageable);
    }

    /**
     * lấy tất  customer với activeStatus = inactive
     *
     * @param customerRequest The customer request
     * @param latestVersion   the latest version
     * @param pageable        Request paging object
     * @return Page<Customer> customer paging object
     */
    public Page<Customer> inactive(@Valid CustomerSearchForm customerRequest, Long latestVersion, Pageable pageable) {
        return customerRepository.findAll(CustomerSpecifications
                .spec()
                .msisdn(customerRequest.getMsisdn())
                .lock(customerRequest.lockRequest())
                .inactive(latestVersion)
                .build(), pageable);
    }

    /**
     * lay trang thai khoan vay cua customer theo cusId
     * trang thai khoan vay la chua tat toan | dang vay -> thong bao MSG_31
     * khoa thanh cong -> thong bao MSG_32
     *
     * @param id The customer id
     */
    @Override
    public void lock(Long id) {
        Customer customer = customerRepository
                .findById(id).orElseThrow(CustomerNotFoundException::new);

        if (customer.getLockStatus() == Customer.LOCK_STATUS.LOCK) {
            throw new CustomerHasBeenLockException();
        }

        customer = customerRepository.customerHasNoLoan(id);
        customer = Optional.ofNullable(customer)
                .orElse(customerRepository.customerValid2Lock(id));

        Optional.ofNullable(customer)
                .map((Customer c) -> {
                    c.setLockStatus(Customer.LOCK_STATUS.LOCK);
                    return customerRepository.save(c);
                })
                .orElseThrow(CustomerLockFailException::new);
    }

    /**
     * nếu customer có trạng thái status = Lock -> thuc hien unlock
     *
     * @param id The customer id
     */
    @Override
    public void unlock(Long id) {
        customerRepository
                .findById(id)
                .map((Customer customer) -> {
                    if (customer.getLockStatus() == Customer.LOCK_STATUS.UNLOCK || customer.getLockStatus() == null) {
                        throw new CustomerHasBeenUnLockException();
                    }
                    customer.setLockStatus(null);
                    return customerRepository.save(customer);
                }).orElseThrow(CustomerNotFoundException::new);
    }

    /**
     * Thực hiện insert hoặc update customer từ danh sách truyền vào
     *
     * @param customers the list of customer
     * @param version   the version object
     */
    @Override
    public void saveWhiteList(List<Customer> customers, Version version) {
        customers.stream().filter(Objects::nonNull).forEach((Customer customer) -> {
            Optional<Customer> optCustomer = customerRepository
                                             .findByMsisdnOrIdentityNumber(customer.getMsisdn(),
                                                                           customer.getIdentityNumber());
            if (optCustomer.isPresent()) {
                customer.setId(optCustomer.get().getId());
                customer.setBatchStatus(BatchStatus.UPDATE);
            } else {
                customer.setAddress(new Address());
                customer.setBatchStatus(BatchStatus.INSERT);
            }
            customer.setVersion(version);
            customerRepository.save(customer);
        });
    }
}


