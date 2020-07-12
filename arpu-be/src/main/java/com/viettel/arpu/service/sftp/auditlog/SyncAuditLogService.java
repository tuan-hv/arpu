/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.service.sftp.auditlog;

@FunctionalInterface
public interface SyncAuditLogService {
    void saveAuditLog(Long versionId, String reason, Long countRecord, String content);
}
