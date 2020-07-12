/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.model.response.mb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author VuHQ
 * @Since 6/26/2020
 */
@Getter
@Setter
@ToString
public class VtOtpConfirmResponse extends MbBaseResponse {
    @JsonProperty("status")
    private VtStatusResponse vtStatusResponse = null;

}
