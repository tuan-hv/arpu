/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */

package com.viettel.arpu.repository;

import com.viettel.arpu.model.entity.Version;
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
class VersionRepositoryTest {

    @Autowired
    VersionRepository versionRepository;

    @Test
    void testGetLatestVersionForBatchId() {
        Long l = versionRepository.getLatestVersionForBatchId(1L);
        then(l).isEqualTo(4L);
    }

    @Test
    void testGetLatestIdForBatchId() {
        Long l = versionRepository.getLatestIdForBatchId(1L);
        then(l).isEqualTo(4L);
    }

    @Test
    void testUpdateVersion() {
        int i = versionRepository.updateVersion(Version.RunStatus.SUCCESS, "Khong noi server", 3l);
        then(i).isEqualTo(1L);
    }
}