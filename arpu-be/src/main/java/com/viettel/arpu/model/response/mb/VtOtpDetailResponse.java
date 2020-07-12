/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.model.response.mb;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author VuHQ
 * @Since 6/25/2020
 */
@Setter
@Getter
@ToString
public class VtOtpDetailResponse {
    private String signedRequest = "";
    private String otp = "";
    private String token = "";
}
