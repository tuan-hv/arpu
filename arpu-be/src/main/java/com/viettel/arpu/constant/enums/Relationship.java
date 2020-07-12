/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.constant.enums;

import lombok.Getter;

/**
 * @Author VuHQ
 * @Since 6/15/2020
 */
@Getter
public enum Relationship {
    MQHS_01("1"),
    MQHS_02("2"),
    MQHS_03("3"),
    MQHS_04("4"),
    MQHS_05("5"),
    MQHS_06("6"),
    MQHS_07("7");

    private String id;

    Relationship(String id) {
        this.id = id;
    }
}
