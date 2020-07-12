package com.viettel.arpu.repository;

import com.viettel.arpu.model.entity.Interest;
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
public class InterestRepositoryTest {
    @Autowired
    InterestRepository interestRepository;

    @Test
    void testFindInterestById() {
        Interest opt = new Interest();
        opt.setId(1000l);
        opt.setStatus("Success");
        opt.setLoanTerm("Success");
        opt.setInterestRate("Success");
        opt.setDescription("Success");
        opt.setName("Success");
        Long id = 1L;
        then(opt).isNotNull();
        then(opt.getDescription()).isNotNull();
        then(opt.getStatus()).isNotNull();
        then(opt.getName()).isNotNull();
        Interest interest = interestRepository.findById(id).orElse(null);
        then(interest).isNotNull();
        assert interest != null;
        then(interest.getId()).isEqualTo(id);
    }

    @Test
    void testEqualsAndHashCode() {
        Interest opt = new Interest();
        Interest optTow = new Interest();
        then(opt).isNotNull();
        then(optTow).isNotNull();
        then(opt.hashCode()).isEqualTo(optTow.hashCode());
        then(opt.equals(optTow));
    }
}
