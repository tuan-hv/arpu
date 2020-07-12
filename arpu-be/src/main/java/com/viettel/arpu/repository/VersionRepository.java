/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.repository;

import com.viettel.arpu.model.entity.Version;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface VersionRepository extends JpaRepository<Version, Long> {

    @Query("SELECT MAX(v.versionSync) FROM Version v WHERE v.batchId =:batchId")
    Long getLatestVersionForBatchId(@Param("batchId") Long batchId);

    @Query("SELECT MAX(v.id) FROM Version v WHERE v.batchId =:batchId")
    Long getLatestIdForBatchId(@Param("batchId") Long batchId);

    @Transactional
    @Modifying
    @Query("update Version  set runStatus =:runStatus , reason =:reason where id =:versionId")
    int updateVersion(@Param("runStatus") Version.RunStatus runStatus, @Param("reason") String reason,
                      @Param("versionId") Long versionId);
}
