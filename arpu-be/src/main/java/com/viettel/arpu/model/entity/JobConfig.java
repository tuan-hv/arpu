/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Table(name = "tbl_job_config")
@Entity
@Getter
@Setter
public class JobConfig {
    @Id
    private Long batchId = null;
    @NotNull
    private String cron = "";
    private String description = "";
}
