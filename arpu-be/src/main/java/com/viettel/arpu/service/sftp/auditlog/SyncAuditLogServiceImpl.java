/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.service.sftp.auditlog;

import com.viettel.arpu.model.entity.SyncAuditLog;
import com.viettel.arpu.service.audit.AuditLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class SyncAuditLogServiceImpl implements SyncAuditLogService {
    private AuditLogService auditLogService;
    @Autowired
    SyncAuditLogServiceImpl(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAuditLog(Long versionId, String reason, Long countRecord, String content) {
        SyncAuditLog syncAuditLog = SyncAuditLog.builder()
                .versionId(versionId)
                .recordNumber(countRecord)
                .recordContent(content)
                .reason(reason)
                .build();
        auditLogService.save(syncAuditLog);
    }
}
