package com.viettel.arpu.repository;

import com.viettel.arpu.model.entity.CustomerRef;
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
public class CustomerRefRepositoryTest {
    @Autowired
    CustomerRefRepository customerRefRepository;

    @Test
    void testFindCustomerRefById() {
        Long id = 1L;
        CustomerRef customerRef = customerRefRepository.findById(id).orElse(null);
        then(customerRef).isNotNull();
        assert customerRef != null;
        then(customerRef.getId()).isEqualTo(id);
    }
}
