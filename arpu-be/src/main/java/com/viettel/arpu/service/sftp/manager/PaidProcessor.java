/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.service.sftp.manager;

import com.viettel.arpu.config.FtpStorageProperties;
import com.viettel.arpu.config.SftpInfo;
import com.viettel.arpu.constant.BatchConstant;
import com.viettel.arpu.model.entity.Version;
import com.viettel.arpu.service.loan.LoanService;
import com.viettel.arpu.service.sftp.Processor;
import com.viettel.arpu.service.sftp.SFTPService;
import com.viettel.arpu.service.sftp.validator.ValidatorService;
import com.viettel.arpu.service.version.VersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Process xử lý đồng bộ tất toán khoản vay
 *
 * @author tuongvx
 */
@Service(value = "paidLoanProcessor")
public class PaidProcessor implements Processor {

    private ValidatorService syncValidatorService;
    private SFTPService sftpService;
    private LoanService loanService;
    private VersionService versionService;
    private FtpStorageProperties ftpStorageProperties;
    private MessageSource messageSource;

    @Autowired
    PaidProcessor(@Qualifier("syncPLValidatorServiceImpl") ValidatorService syncValidatorService,
                  SFTPService sftpService, LoanService loanService,
                  VersionService versionService, FtpStorageProperties ftpStorageProperties,
                  @Qualifier("validatorMessageSource") MessageSource messageSource) {
        this.syncValidatorService = syncValidatorService;
        this.sftpService = sftpService;
        this.loanService = loanService;
        this.versionService = versionService;
        this.ftpStorageProperties = ftpStorageProperties;
        this.messageSource = messageSource;
    }
    /**
     * Đồng bộ dữ liệu tất toán khoản vay
     *
     * @throws Exception
     */
    @Override
    public void process() throws Exception {
        DataMapSFTP dataMapSFTP = new DataMapSFTP(ftpStorageProperties, messageSource,
                                                  sftpService, versionService).invoke(BatchConstant.PAID_LOAN);
        Boolean isConnected = dataMapSFTP.isConnectedToSFTPServer();
        if (Boolean.TRUE.equals(isConnected)) {
            updateService(dataMapSFTP);
        }
        else {
            throw new IOException(messageSource.getMessage(BatchConstant.SYNC_CONNECT_FAILED, null, new Locale("vi")));
        }
    }

    /**
     * Method thực hiện viêc Gọi các method validate
     * Từ danh sách các bản ghi hợp lệ sẽ thực hiện cập nhật nghiệp vụ tương ứng
     *
     * @param dataMapSFTP
     * @throws Exception
     */
    private void updateService(DataMapSFTP dataMapSFTP) throws Exception {
        Version version = dataMapSFTP.getVersion();
        List<String> records = validateAndLogFail(dataMapSFTP, version);
        boolean isExistRecord = !records.isEmpty();
        if (Boolean.TRUE.equals(isExistRecord)) {
            loanService.updatePayLoan(records);
        } else {
            versionService.updateVersion(version.getId(),
                    Version.RunStatus.FAILED,
                    messageSource.getMessage(BatchConstant.SYNC_NON_RECORD_PASS_VALID, null, new Locale("vi")));
        }

    }

    /**
     * Method thực hiện việc validate danh sách các bản ghi sau khi lấy từ file serverSFTp về
     * Trả về danh sách đối tượng sau khi validate
     *
     * @param dataMapSFTP
     * @param version
     * @return
     * @throws Exception
     */
    private List<String> validateAndLogFail(DataMapSFTP dataMapSFTP, Version version) throws Exception {
        SftpInfo sftpInfoEx = dataMapSFTP.getSftpInfoEx();
        List<String> data = sftpService.readFileFromSFTP(sftpInfoEx);
        return syncValidatorService
                .validatePayLoanList(data, version);
    }
}
