/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.model.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity(name = "CodeCode")
@Table(name = "tbl_code_code")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CodeCode implements Serializable {

    private static final long serialVersionUID = 807789452230350781L;

    @Id
    @EqualsAndHashCode.Include
    private String id;

    public CodeCode() {

    }

    public CodeCode(String id) {
        this.id = id;
    }

    private String codeName;

    private String codeType;

    private String codeTypeName;
}
