/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
public class SftpInfo {
    // Create the SFTP URI using the host name, userid, password, remote, path and file name
    private String username;
    private String password;
    private String serverAddress;
    private int port;
    private String path2File;
}
