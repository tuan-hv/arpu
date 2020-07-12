/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.service.audit;

import com.viettel.arpu.model.entity.SyncAuditLog;

@FunctionalInterface
public interface AuditLogService {
    void save(SyncAuditLog syncAuditLog);
}
