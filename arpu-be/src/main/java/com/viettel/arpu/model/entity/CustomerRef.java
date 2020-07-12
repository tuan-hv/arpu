/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity(name = "CustomerRef")
@Table(name = "tbl_customer_ref")
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class CustomerRef extends AbstractAuditingEntity {
    private static final long serialVersionUID = -4364053321926465698L;
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    /**
     * mối quan hệ : họ hàng, cha/mẹ,anh/em ruột...
     */
    @ManyToOne
    private CodeCode relationship;
    /**
     * customer
     */
    @ManyToOne
    @JsonIgnore
    private Customer customer;
    /**
     * người tham chiếu
     */
    @ManyToOne(cascade = CascadeType.ALL)
    private Reference reference;
}
