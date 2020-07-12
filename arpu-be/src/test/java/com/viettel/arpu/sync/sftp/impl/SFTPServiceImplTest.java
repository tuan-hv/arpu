package com.viettel.arpu.sync.sftp.impl;

import com.viettel.arpu.config.FtpStorageProperties;
import com.viettel.arpu.config.SftpInfo;
import com.viettel.arpu.constant.BatchConstant;
import com.viettel.arpu.exception.FtpException;
import com.viettel.arpu.service.sftp.SFTPService;
import com.viettel.arpu.service.sftp.SFTPServiceImpl;
import com.viettel.arpu.service.sftp.manager.DataMapSFTP;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;

/**
 * @author tuongvx
 * @created 28/06/2020 - 2:28 PM
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SFTPServiceImplTest {
    @Qualifier("retrySync")
    @Autowired
    private RetryTemplate retryTemplate;
    @Autowired
    private FtpStorageProperties ftpStorageProperties;

    SFTPService sftpService;

    @Test
    public void testItShouldBeSReadFileFromSFTPSuccessForWhiteList() throws Exception {
        SftpInfo sftpInfoEx = SftpInfo.builder()
                .password(ftpStorageProperties.getBatchPwd())
                .path2File(ftpStorageProperties.getWhitelistFile())
                .port(Integer.parseInt(ftpStorageProperties.getPort()))
                .username(ftpStorageProperties.getBatchUser())
                .serverAddress(ftpStorageProperties.getHost())
                .build();
        sftpService = new SFTPServiceImpl(retryTemplate);
        List<String> lst = sftpService.readFileFromSFTP(sftpInfoEx);
        then(lst).isNotEmpty();
        then(lst.size()).isGreaterThan(10);
    }


    @Test
    public void testItShouldBeSReadFileFromSFTPSuccessForPaidLoan() throws Exception {
        SftpInfo sftpInfoEx = SftpInfo.builder()
                .password(ftpStorageProperties.getBatchPwd())
                .path2File(ftpStorageProperties.getPayLoanFile())
                .port(Integer.parseInt(ftpStorageProperties.getPort()))
                .username(ftpStorageProperties.getBatchUser())
                .serverAddress(ftpStorageProperties.getHost())
                .build();
        sftpService = new SFTPServiceImpl(retryTemplate);
        List<String> lst = sftpService.readFileFromSFTP(sftpInfoEx);
        then(lst).isNotEmpty();
        then(lst.size()).isGreaterThan(4);
    }

    @Test
    public void testItShouldBeSReadFileFromSFTPSuccessWithFailed() throws Exception {
        try {
            SftpInfo sftpInfoEx = SftpInfo.builder()
                    .password("12345")
                    .path2File(ftpStorageProperties.getPayLoanFile())
                    .port(Integer.parseInt(ftpStorageProperties.getPort()))
                    .username(ftpStorageProperties.getBatchUser())
                    .serverAddress(ftpStorageProperties.getHost())
                    .build();
            sftpService = new SFTPServiceImpl(retryTemplate);
            sftpService.readFileFromSFTP(sftpInfoEx);
        } catch (FtpException ex) {
            FtpException exception = new FtpException(new IOException());
            then(exception.getMessage()).isEqualTo(ex.getMessage());
        }
    }
}
