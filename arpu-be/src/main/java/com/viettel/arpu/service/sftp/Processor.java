/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.service.sftp;

/**
 * Interface process đồng bộ whitelist và tất toán khoản vay
 *
 * @author tuongvx
 */
@FunctionalInterface
public interface Processor {
    void process() throws Exception;
}
