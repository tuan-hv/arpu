/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.constant;

import org.springframework.http.HttpHeaders;

/**
 * @Author VuHQ
 * @Since 6/8/2020
 */
public class ParamsHeader {
    private HttpHeaders mapHeader = new HttpHeaders();

    public static ParamsHeader paramsHeader(){
        return new ParamsHeader();
    }

    public ParamsHeader serviceChanel(String serviceChanel){
        this.mapHeader.set("serviceChanel", serviceChanel);
        return this;
    }

    public ParamsHeader serviceCode(String serviceCode){
        this.mapHeader.set("serviceCode", serviceCode);
        return this;
    }

    public ParamsHeader serviceIndicator(String serviceIndicator){
        this.mapHeader.set("serviceIndicator", serviceIndicator);
        return this;
    }

    public ParamsHeader systemTrace(String systemTrace){
        this.mapHeader.set("systemTrace", systemTrace);
        return this;
    }

    public ParamsHeader setRequestId(String requestId) {
        this.mapHeader.set("requestId", requestId);
        return this;
    }

    public ParamsHeader product(String product) {
        this.mapHeader.set("Product", product);
        return this;
    }

    public ParamsHeader xRequestId(String xRequestId) {
        this.mapHeader.set("X-Request-Id", xRequestId);
        return this;
    }

    public ParamsHeader appToken(String appToken) {
        this.mapHeader.set("App-Token", appToken);
        return this;
    }

    public HttpHeaders build(){
        return this.mapHeader;
    }

}
