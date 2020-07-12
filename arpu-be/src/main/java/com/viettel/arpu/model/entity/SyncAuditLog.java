/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Table(name = "tbl_sync_audit_log")
@Entity(name = "SyncAuditLog")
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class SyncAuditLog extends AbstractAuditingEntity {
    private static final long serialVersionUID = -80587589036753036L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private Long versionId;
    @NotNull
    private Long recordNumber;
    @NotNull
    @Column(length = 500)
    private String recordContent;
    @NotNull
    @Column(length = 5000)
    private String reason;

}
