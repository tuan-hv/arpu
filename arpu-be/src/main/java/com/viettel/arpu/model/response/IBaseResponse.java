/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface IBaseResponse<T> {
    @JsonIgnore
    boolean isEmpty();

    T getData();
}
