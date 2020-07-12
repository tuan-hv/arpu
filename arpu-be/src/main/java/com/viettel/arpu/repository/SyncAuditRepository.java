/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.repository;

import com.viettel.arpu.model.entity.SyncAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SyncAuditRepository extends JpaRepository<SyncAuditLog, Long> {
    /**
     * Lấy tất cả bản ghi theo ID của version truyền vào
     * Trả về một list
     *
     * @param versionId
     * @return
     */
    List<SyncAuditLog> findAllByVersionId(Long versionId);

    List<SyncAuditLog> findAllByVersionIdAndAndRecordNumber(Long versionId, Long recordNumber);
}
