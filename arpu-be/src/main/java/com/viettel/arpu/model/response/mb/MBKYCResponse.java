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
 * @author DoDV
 * @Date :6/17/2020, Wed
 */

/**
 * Response trả về khi call API của MB
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MBKYCResponse extends MbBaseResponse {
    private String status;
}
