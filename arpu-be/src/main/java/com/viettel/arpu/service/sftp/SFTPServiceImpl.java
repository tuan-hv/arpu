/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.service.sftp;

import com.viettel.arpu.config.SftpInfo;
import com.viettel.arpu.exception.FtpException;
import com.viettel.arpu.utils.FtpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Service implement thực hiện đọc file từ server SFTP
 *
 * @author tuongvx
 */
@Service
@Slf4j
public class SFTPServiceImpl implements SFTPService {
    private List<String> inputList = new ArrayList<>();
    private RetryTemplate retryTemplate;
    @Autowired
    public SFTPServiceImpl(@Qualifier("retrySync") RetryTemplate retryTemplate) {
        this.retryTemplate = retryTemplate;
    }

    /**
     * Thực hiện đọc file từ server
     * Trả về danh sách data trong file nếu connect server thành công
     *
     * @param sftpInfo
     * @return
     * @throws Exception
     */
    @Override
    public List<String> readFileFromSFTP(final SftpInfo sftpInfo) throws Exception {
        RetryCallback<List<String>, Exception> callbackFunc = (RetryContext retryCallback) -> {
            log.info("Attemptat {} time(s)", retryCallback.getRetryCount() + 1);
            InputStream stream = new ByteArrayInputStream(FtpUtils.fileInBytesFromFtp(sftpInfo));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
            inputList = bufferedReader.lines().skip(1).collect(Collectors.toList());
            return inputList.stream().filter(Objects::nonNull).collect(Collectors.toList());
        };
        retryTemplate.execute(callbackFunc, (RetryContext recoverCallback) -> {
            log.info("Recovering call back");
            throw new FtpException(new IOException());
        });
        return inputList.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }
}
