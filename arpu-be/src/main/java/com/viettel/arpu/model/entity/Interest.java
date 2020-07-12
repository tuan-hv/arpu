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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity(name = "Interest")
@Table(name = "tbl_interest")
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Interest extends AbstractAuditingEntity {
    private static final long serialVersionUID = 5535617283008955255L;
    /**
     * id của bản ghi
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    /**
     * Trạng thái lãi suất
     */
    private String status;
    /**
     * Kì hạn vay
     */
    private String loanTerm;
    /**
     * Tỉ lệ lãi suất
     */
    private String interestRate;
    /**
     * Mô tả lãi suất
     */
    private String description;
    /**
     * Tên lãi suất
     */
    private String name;

}
