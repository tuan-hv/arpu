/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.controller.sftp;

import com.viettel.arpu.service.sftp.manager.PaidProcessor;
import com.viettel.arpu.service.sftp.manager.SFTPManager;
import com.viettel.arpu.service.sftp.manager.WhiteListProcessor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

/**
 * Controller để call api đồng bộ danh sách whitelist và tất toán khoản vay
 *
 * @author tuonvx
 */
@Controller
@RequestMapping("/api")
@Slf4j
public class SyncController {
    private WhiteListProcessor whiteListProcess;
    private PaidProcessor paidProcess;
    private static final String SUCCESS = "Success";
    private static final String FAILED = "Failed";

    @Autowired
    public SyncController(WhiteListProcessor whiteListProcess, PaidProcessor paidProcess) {
        this.whiteListProcess = whiteListProcess;
        this.paidProcess = paidProcess;
    }

    /**
     * Getmapping đồng bộ danh sách whitelist
     * Nlếu Không kết nối được server thì trả về message kết nối lỗi
     *
     * @return
     */
    @SneakyThrows
    @GetMapping("/sync/whitelist")
    @ResponseBody
    public Map<String, String> syncWhiteList() {
        try {
            SFTPManager sftpManager = new SFTPManager().withWhiteListProcess(whiteListProcess).build();
            sftpManager.readBy(SFTPManager.Tasks.WHITE_LIST).process();
            return Collections.singletonMap(SUCCESS, SUCCESS);
        } catch (IOException e) {
            log.error("Error syncWhiteList: " + e);
            return Collections.singletonMap(FAILED, e.getMessage());
        }
    }

    /**
     * Getmapping đồng bộ danh sách tất toán khoản vay
     * Nếu Không kết nối được server thì trả về message kết nối lỗi
     *
     * @return
     */
    @SneakyThrows
    @GetMapping("/sync/payloan")
    @ResponseBody
    public Map<String, String> syncPaidLoan() {
        try {
            SFTPManager sftpManager = new SFTPManager().withPaidProcess(paidProcess).build();
            sftpManager.readBy(SFTPManager.Tasks.PAID).process();
            return Collections.singletonMap(SUCCESS, SUCCESS);
        } catch (IOException e) {
            log.error("Error syncPaidLoan: " + e);
            return Collections.singletonMap(FAILED, e.getMessage());
        }
    }
}
