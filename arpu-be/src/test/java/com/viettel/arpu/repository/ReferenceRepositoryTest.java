/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */

package com.viettel.arpu.repository;

import com.viettel.arpu.model.entity.Reference;
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
class ReferenceRepositoryTest {

    @Autowired
    private ReferenceRepository referenceRepository;

    @Test
    void testFindByMsisdn() {
        final String msisdn = "0368774849";
        Reference reference = referenceRepository.findByMsisdn(msisdn).orElse(null);
        then(reference).isNotNull();
        then(reference.getMsisdn()).isEqualTo("0368774849");
    }

}
