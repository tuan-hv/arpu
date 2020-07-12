/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Optional;

/**
 * @author trungnb3
 * @Date :5/26/2020, Tue
 */
@Getter
@Setter
public class CustomerSearchForm {
    public enum LOCK_REQUEST {
        ALL, LOCK, UNLOCK;

        public static LOCK_REQUEST of(String name) {
            for(LOCK_REQUEST prop : values()){
                if(prop.name().equals(name)){
                    return prop;
                }
            }
            return ALL;
        }
    }


    @Size(max = 12)
    private String msisdn = "";

    @NotBlank
    @Pattern(regexp = "^lock|unlock|all$")
    private String lockStatus = "";

    @NotBlank
    @Pattern(regexp = "^active|inactive|all$")
    private String activeStatus = "";

    public LOCK_REQUEST lockRequest() {
        return Optional.ofNullable(lockStatus).map(
                s -> LOCK_REQUEST.of(lockStatus.toUpperCase())).orElse(LOCK_REQUEST.ALL);
    }
}
