/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.service.sftp.manager;

import com.viettel.arpu.config.FtpStorageProperties;
import com.viettel.arpu.config.SftpInfo;
import com.viettel.arpu.constant.BatchConstant;
import com.viettel.arpu.exception.FtpException;
import com.viettel.arpu.model.entity.Version;
import com.viettel.arpu.service.version.VersionService;
import com.viettel.arpu.service.sftp.SFTPService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;

import java.util.Locale;
import java.util.Objects;

/**
 * Xử lý thông tin kết nối server và kiểm tra kết nối có được hay không
 *
 * @author tuongvx
 */
@Slf4j
public class DataMapSFTP {
    private Version version;
    private SftpInfo sftpInfoEx;
    private FtpStorageProperties ftpStorageProperties;
    @Qualifier("validatorMessageSource")
    private MessageSource messageSource;
    private SFTPService sftpService;
    private VersionService versionService;

    public DataMapSFTP(FtpStorageProperties ftpStorageProperties, MessageSource messageSource,
                       SFTPService sftpService, VersionService versionService) {
        this.ftpStorageProperties = ftpStorageProperties;
        this.messageSource = messageSource;
        this.sftpService = sftpService;
        this.versionService = versionService;
    }

    public Version getVersion() {
        return version;
    }

    public SftpInfo getSftpInfoEx() {
        return sftpInfoEx;
    }

    /**
     * Trả về Version mới nhất và thông tin kết nối server từng loại đồng bộ
     * server
     *
     * @return
     */
    public DataMapSFTP invoke(String type) {
        version = versionService.createPayLoanBatch();
        if (Objects.equals(type, BatchConstant.WHITE_LIST)) {
            version = versionService.createWhiteListBatch();
        }
        String path2File = ftpStorageProperties.getPayLoanFile();
        if (Objects.equals(type, BatchConstant.WHITE_LIST)) {
            path2File = ftpStorageProperties.getWhitelistFile();
        }
        sftpInfoEx = SftpInfo.builder()
                .password(ftpStorageProperties.getBatchPwd())
                .path2File(path2File)
                .port(Integer.parseInt(ftpStorageProperties.getPort()))
                .username(ftpStorageProperties.getBatchUser())
                .serverAddress(ftpStorageProperties.getHost())
                .build();
        return this;
    }

    /**
     * Method kiểm tra thông tin kết nối có oke không
     * Nếu không thì cập nhật lại lí do thất bại cho version khi đồng bộ
     *
     * @return
     * @throws Exception
     */
    public boolean isConnectedToSFTPServer() throws Exception {
        try {
            sftpService.readFileFromSFTP(sftpInfoEx);
            return true;
        } catch (FtpException e) {
            log.error("Error read file from sFTP: " + e);
            versionService.updateVersion(version.getId(),
                    Version.RunStatus.FAILED,
                    messageSource.getMessage(BatchConstant.SYNC_CONNECT_FAILED, null, new Locale("vi")));
            return false;
        }
    }
}
