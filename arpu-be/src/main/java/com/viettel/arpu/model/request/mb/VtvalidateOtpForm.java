/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.model.request.mb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @Author VuHQ
 * @Since 6/25/2020
 */
@Getter
@Setter
@ToString
public class VtvalidateOtpForm {
    private List<String> type = null;
    private String msisdn = "";
    @JsonProperty("payload")
    private PayLoadForm payLoad = null;
}
