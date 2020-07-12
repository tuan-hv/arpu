/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.service.sftp.manager;

import com.viettel.arpu.service.sftp.Processor;

/**
 * Quản các process đồng bộ
 * @author tuongvx
 */
public class SFTPManager {
    private PaidProcessor paidProcess;
    private WhiteListProcessor whiteListProcess;

    /**
     * Khởi tạo process đồng bộ thấu chi
     * @param paidProcess
     * @return
     */
    public SFTPManager withPaidProcess(PaidProcessor paidProcess) {
        this.paidProcess = paidProcess;
        return this;
    }

    /**
     * Khởi tạo process đồng bộ whitelist
     * @param whiteListProcess
     * @return
     */
    public SFTPManager withWhiteListProcess(WhiteListProcessor whiteListProcess) {
        this.whiteListProcess = whiteListProcess;
        return this;
    }

    public SFTPManager build() {
        return this;
    }

    public Processor readBy(Tasks task) throws Exception {
        switch (task) {
            case PAID:
                return paidProcess;
            case WHITE_LIST:
                return whiteListProcess;
        }
        throw new UnsupportedOperationException("Not Support");
    }

    public enum Tasks {
        WHITE_LIST,
        PAID,
        FAILED;
    }
}
