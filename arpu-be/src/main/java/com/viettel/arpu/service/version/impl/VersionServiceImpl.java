/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.service.version.impl;

import com.viettel.arpu.constant.BatchConstant;
import com.viettel.arpu.model.entity.Version;
import com.viettel.arpu.repository.VersionRepository;
import com.viettel.arpu.service.version.VersionService;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service implement để tạo bản version mới nhất cho từng loại batch
 *
 * @author tuongvx
 */
@Service
@Slf4j
public class VersionServiceImpl implements VersionService {
    private VersionRepository versionRepository;

    @Autowired
    VersionServiceImpl(VersionRepository versionRepository) {
        this.versionRepository = versionRepository;
    }

    private static class Batch {
        private Long batchId;
        private String batchName;

        public Batch(Long batchId, String batchName) {
            this.batchId = batchId;
            this.batchName = batchName;
        }
    }

    private Batch WHITE_LIST = new Batch(BatchConstant.WHITE_LIST_BATCH_ID, BatchConstant.WHITE_LIST);
    private Batch PAY_LOAN = new Batch(BatchConstant.PAY_LOAN_BATCH_ID, BatchConstant.PAID_LOAN);

    /**
     * @param batch
     * @return
     */
    private Version createVersionByBatch(Batch batch) {
        Long maxVersion = versionRepository.getLatestVersionForBatchId(batch.batchId);
        Long _version = Optional.ofNullable(maxVersion)
                .map(it -> it + 1)
                .orElse(BatchConstant.INIT_VERSION_VALUE);
        Version entity = new Version();
        entity.setVersionSync(_version);
        entity.setBatchId(batch.batchId);
        entity.setBatchName(batch.batchName);
        entity.setRunStatus(Version.RunStatus.SUCCESS);
        versionRepository.save(entity);
        log.info("Latest version for {} is : {} ", batch.batchName, _version);
        return entity;
    }

    @Override
    public Version createWhiteListBatch() {
        return createVersionByBatch(WHITE_LIST);
    }

    @Override
    public Version createPayLoanBatch() {
        return createVersionByBatch(PAY_LOAN);
    }

    @Override
    public Long getLatestVersionForBatchId(Long batchId) {
        return versionRepository.getLatestVersionForBatchId(batchId);
    }

    @Override
    public int updateVersion(Long versionId, Version.RunStatus runStatus, String reason) {
        return versionRepository.updateVersion(runStatus, reason, versionId);
    }
}
