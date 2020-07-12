package com.viettel.arpu.repository;

import com.viettel.arpu.model.entity.CodeCode;
import com.viettel.arpu.model.entity.Loan;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;

@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional
class LoanRepositoryTest {

    @Autowired
    LoanRepository loanRepository;
    @Autowired
    CodeCodeRepository codeCodeRepository;


    @Test
    void testUpdateApproveForLoanWithSuccessGreaterThanThreeRecord() {

        CodeCode codeCode = codeCodeRepository.findById("KVS_04").orElse(null);
        then(codeCode).isNotNull();
        int count = loanRepository.updateAVStatusByLoanAcc(codeCode, "ABC");
        then(count).isNotNull();
        then(count).isGreaterThan(3);
    }

    @Test
    void testUpdateApproveForLoanWithSuccessOneRecord() {
        CodeCode codeCode = codeCodeRepository.findById("KVS_04").orElse(null);
        then(codeCode).isNotNull();
        int count = loanRepository.updateAVStatusByLoanAcc(codeCode, "Tungtq2");
        then(count).isNotNull();
        then(count).isEqualTo(1);
    }

    @Test
    void testUpdateApproveWithLoanAccountNotFound() {
        CodeCode codeCode = codeCodeRepository.findById("123455").orElse(null);
        then(codeCode).isNull();
        int count = loanRepository.updateAVStatusByLoanAcc(codeCode, "Tungtq290");
        then(count).isEqualTo(0);
    }

    @Test
    void testFindFirstByLoanAccount() {
        Optional<Loan> loan = loanRepository.findFirstByLoanAccount("ABC");
        then(loan).isNotEmpty();
    }

}
