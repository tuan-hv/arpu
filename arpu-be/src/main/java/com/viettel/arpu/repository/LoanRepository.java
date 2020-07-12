/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.repository;

import com.viettel.arpu.model.entity.CodeCode;
import com.viettel.arpu.model.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


/**
 * @Author VuHQ
 * @Since 5/25/2020
 */
@Repository
public interface LoanRepository extends JpaRepository<Loan, Long>, JpaSpecificationExecutor<Loan> {


    @Transactional
    @Modifying
    @Query("update Loan  set loanStatus =:loanStatus where loanAccount =:loanAccount")
    int updateAVStatusByLoanAcc(@Param("loanStatus") CodeCode loanStatus, @Param("loanAccount") String loanAccount);

    Optional<Loan> findFirstByLoanAccount(String loanAccount);
}
