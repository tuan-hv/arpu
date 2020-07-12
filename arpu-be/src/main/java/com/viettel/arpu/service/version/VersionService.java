/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.service.version;

import com.viettel.arpu.model.entity.Version;
/**
 * @author trungnb3
 * @Date :6/3/2020, Wed
 */
public interface VersionService {
    Version createWhiteListBatch();

    Version createPayLoanBatch();

    Long getLatestVersionForBatchId(Long batchId);

    int updateVersion(Long versionId, Version.RunStatus runStatus, String reason);
}
