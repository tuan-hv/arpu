package com.viettel.arpu.service.customer.impl;

import com.viettel.arpu.constant.BatchConstant;
import com.viettel.arpu.constant.enums.Gender;
import com.viettel.arpu.model.entity.Address;
import com.viettel.arpu.model.entity.Customer;
import com.viettel.arpu.model.entity.Version;
import com.viettel.arpu.repository.CustomerRepository;
import com.viettel.arpu.service.customer.CustomerService;
import com.viettel.arpu.service.version.VersionService;
import com.viettel.arpu.utils.BatchStatus;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;

/**
 * @author tuongvx
 * @created 28/06/2020 - 3:08 PM
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerServiceImplTest {
    @Autowired
    CustomerService customerService;
    @Autowired
    VersionService versionService;
    @Autowired
    CustomerRepository customerRepository;

    @Test
    public void testSaveWhiteListWithInsert() throws Exception {
        Version entity = versionService.createWhiteListBatch();
        then(entity).isNotNull();
        Customer customer = Customer.builder()
                .fullName("Tuong Vu")
                .msisdn("098576346")
                .dateOfBirth(LocalDate.now())
                .gender(Gender.MALE)
                .identityType("CCCD")
                .identityNumber("123")
                .dateOfIssue(LocalDate.now())
                .placeOfIssue("HN")
                .scoreMin(1)
                .scoreMax(12)
                .viettelpayWallet("123")
                .arpuLatestThreeMonths(BigDecimal.valueOf(23l))
                .address(new Address())
                .build();
        List<Customer> customers = new ArrayList<>();
        customers.add(customer);
        then(customers).isNotEmpty();
        then(customers.size()).isEqualTo(1);
        customerService.saveWhiteList(customers, entity);
        Optional<Customer> opt = customerRepository.findById(14l);
        then(opt).isNotEmpty();
        then(opt.get().getBatchStatus()).isEqualTo(BatchStatus.INSERT);
    }

    @Test
    public void testSaveWhiteListWithUpdate() throws Exception {
        Version entity = versionService.createWhiteListBatch();
        then(entity).isNotNull();
        Customer customer = Customer.builder()
                .fullName("Tuong Vu")
                .msisdn("0356447841")
                .dateOfBirth(LocalDate.now())
                .gender(Gender.MALE)
                .identityType("CCCD")
                .identityNumber("124678")
                .dateOfIssue(LocalDate.now())
                .placeOfIssue("HN")
                .scoreMin(1)
                .scoreMax(12)
                .viettelpayWallet("123")
                .arpuLatestThreeMonths(BigDecimal.valueOf(23l))
                .address(new Address())
                .build();
        List<Customer> customers = new ArrayList<>();
        customers.add(customer);
        then(customers).isNotEmpty();
        then(customers.size()).isEqualTo(1);
        customerService.saveWhiteList(customers, entity);
        Optional<Customer> opt = customerRepository.findById(1l);
        then(opt).isNotEmpty();
        then(opt.get().getBatchStatus()).isEqualTo(BatchStatus.UPDATE);
    }

    @Test
    public void testSaveWhiteListWithListEmpty() throws Exception {
        Version entity = versionService.createWhiteListBatch();
        List<Customer> customers = new ArrayList<>();
        then(customers).isEmpty();
        customerService.saveWhiteList(customers, entity);
        List<Customer> opt = (List<Customer>) customerRepository.findAll();
        then(opt).isNotEmpty();
        then(opt.size()).isGreaterThanOrEqualTo(13);
    }
}
