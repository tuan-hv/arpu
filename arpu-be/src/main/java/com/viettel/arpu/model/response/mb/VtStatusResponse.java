/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.model.response.mb;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Author VuHQ
 * @Since 6/23/2020
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VtStatusResponse {
    private String code;
    private String message;
}
