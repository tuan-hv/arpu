/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author trungnb3
 * @Date :5/29/2020, Fri
 */
@Entity(name = "LogApi")
@Table(name = "tbl_log_api")
@Getter
@Setter
public class LogApi extends AbstractAuditingEntity {
    private static final long serialVersionUID = -1745176510353528159L;
    /**
     * requestId
     */
    @Id
    @Column(name = "requestId")
    private String requestId;
    /**
     * request header info
     */
    @Column(name = "request_header_info",length = 65535,columnDefinition="Text")
    private String requestHeaderInfo;
    /**
     * request body info
     */
    @Column(name = "request_body_info",length = 65535,columnDefinition="Text")
    private String requestBodyInfo;
    /**
     * response body info
     */
    @Column(name = "response_body_info",length = 65535,columnDefinition="Text")
    private String responseBodyInfo;
}
