package com.viettel.arpu.repository;

import com.viettel.arpu.model.entity.CodeCode;
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
public class CodeCodeRepositoryTest {
    @Autowired
    CodeCodeRepository codeCodeRepository;

    @Test
    void testFindCodeById() {
        String codeId = "PDS_02";
        CodeCode codeCode = codeCodeRepository.findById(codeId).orElse(null);
        then(codeCode.getId()).isEqualTo(codeId);
        then(codeCode).isNotNull();
    }
}
