/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.model.response.mb;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @Author VuHQ
 * @Since 6/29/2020
 */
@Getter
@Setter
@ToString
public class MbHistoriesDetailResponse {
    private LocalDateTime transactionDate;
    private String amount;
    private String currency;
    private String referenceNumber;
}
