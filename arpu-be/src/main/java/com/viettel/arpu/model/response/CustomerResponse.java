/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.model.response;

import com.viettel.arpu.model.dto.CustomerDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
public class CustomerResponse extends PageResponse<Page<CustomerDTO>> {
    private Long latestVersion;

    public CustomerResponse(Page<CustomerDTO> page, Long latestVersion) {
        super(page);
        this.latestVersion = latestVersion;
    }

}
