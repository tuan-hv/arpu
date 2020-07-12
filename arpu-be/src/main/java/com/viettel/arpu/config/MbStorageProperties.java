/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author VuHQ
 * @Since 6/8/2020
 */
@ConfigurationProperties(prefix = "mb.api")
@Getter
@Setter
public class MbStorageProperties {
    private String checkloan = "";
    private String createloan = "";
    private String reducelimit = "";
    private String increaselimit = "";
    private String finalloan = "";
    private String payloan = "";
    private String historyloan = "";
    private String detailloan = "";
    private String kyc = "";
    private String validatePin = "";
    private int readTimeout = 0;
    private int requestTimeout = 0;
}
