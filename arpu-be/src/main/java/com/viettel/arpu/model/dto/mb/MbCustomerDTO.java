/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.model.dto.mb;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.viettel.arpu.model.dto.ReferenceDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * @Author VuHQ
 * @Since 6/11/2020
 */
@Getter
@Setter
public class MbCustomerDTO{
    @JsonProperty("personalInfo")
    private MbCustomerInfoDTO mbCustomerInfoDTO;

    @JsonProperty("relationship")
    private Set<ReferenceDTO> listReference;
}
