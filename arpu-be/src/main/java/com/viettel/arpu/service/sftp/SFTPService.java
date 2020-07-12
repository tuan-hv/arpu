/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.service.sftp;

import com.viettel.arpu.config.SftpInfo;

import java.util.List;

@FunctionalInterface
public interface SFTPService {

    /**
     * Returns List record from SFTP server.
     */
     List<String> readFileFromSFTP(final SftpInfo sftpInfo) throws Exception;
}
