/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.model.request.mb;

import com.viettel.arpu.constant.MBConstant;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author VuHQ
 * @Since 6/26/2020
 */
@Getter
@Setter
public class VtConfirmForm {
    private String verifyMethod = MBConstant.VERIFY_METHOD;
    private String otp = "";
}
