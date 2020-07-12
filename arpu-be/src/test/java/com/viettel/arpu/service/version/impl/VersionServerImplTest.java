package com.viettel.arpu.service.version.impl;

import com.viettel.arpu.constant.BatchConstant;
import com.viettel.arpu.model.entity.Version;
import com.viettel.arpu.service.version.VersionService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;

import static org.assertj.core.api.BDDAssertions.then;

/**
 * @author tuongvx
 * @created 27/06/2020 - 3:39 PM
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class VersionServerImplTest {

    @Autowired
    VersionService versionService;


    @Test
    public void testCreateWhiteListBatch() {
        Version entity = new Version();
        entity.setVersionSync(5l);
        entity.setBatchId(1l);
        entity.setBatchName(BatchConstant.WHITE_LIST);
        entity.setReason(BatchConstant.WHITE_LIST);
        entity.setCreatedDate(Instant.now());
        entity.setRunStatus(Version.RunStatus.SUCCESS);
        Version opt = versionService.createWhiteListBatch();
        then(opt).isNotNull();
        then(opt.getBatchId()).isEqualTo(entity.getBatchId());
        then(opt.getVersionSync()).isEqualTo(entity.getVersionSync());
        then(opt.getReason()).isNull();
        then(opt.getCreatedDate()).isNotNull();
        then(opt.getBatchName()).isEqualTo(entity.getBatchName());
        then(opt.getRunStatus()).isEqualTo(entity.getRunStatus());
    }


    @Test
    public void testCreatePayLoanBatch() {
        Version entity = new Version();
        entity.setId(1000l);
        entity.setVersionSync(5l);
        entity.setBatchId(2l);
        entity.setBatchName(BatchConstant.PAID_LOAN);
        entity.setReason(BatchConstant.WHITE_LIST);
        entity.setCreatedDate(Instant.now());
        entity.setRunStatus(Version.RunStatus.SUCCESS);
        Version opt = versionService.createPayLoanBatch();
        then(opt).isNotNull();
        then(opt.getBatchId()).isEqualTo(entity.getBatchId());
        then(opt.getVersionSync()).isEqualTo(entity.getVersionSync());
        then(opt.getBatchName()).isEqualTo(entity.getBatchName());
        then(opt.getReason()).isNull();
        then(opt.getCreatedDate()).isNotNull();
        then(opt.getBatchName()).isEqualTo(entity.getBatchName());
        then(opt.getRunStatus()).isEqualTo(entity.getRunStatus());
    }


    @Test
    public void testGetLatestVersionForBatchIdIs1() {
        Long version = versionService.getLatestVersionForBatchId(1l);
        then(version).isNotNull();
        then(version).isEqualTo(4l);
    }

    @Test
    public void testGetLatestVersionForBatchIdIs2() {
        Long version = versionService.getLatestVersionForBatchId(2l);
        then(version).isNotNull();
        then(version).isEqualTo(4l);
    }


    @Test
    public void testUpdateVersionWithRunStatusIsSuccess() {
        int version = versionService.updateVersion(1l, Version.RunStatus.SUCCESS, null);
        then(version).isNotNull();
        then(version).isEqualTo(1);
    }


    @Test
    public void updateVersionWithRunStatusIsFailed() {
        int version = versionService.updateVersion(2l, Version.RunStatus.FAILED, "Connect server failed");
        then(version).isNotNull();
        then(version).isEqualTo(1);
    }
}
