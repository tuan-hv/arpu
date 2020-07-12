/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.constant;

import com.viettel.arpu.constant.enums.ServiceChanel;

/**
 * @Author VuHQ
 * @Since 6/8/2020
 */
public class HeaderConstants {
    public static final ParamsHeader POST_CHECK_LOAN = ParamsHeader.paramsHeader()
            .serviceChanel(ServiceChanel.COUNTER.getCode())
            .serviceCode("052001").serviceIndicator("").systemTrace("post check loan");

    public static final ParamsHeader POST_CREATE_LOAN = ParamsHeader.paramsHeader()
            .serviceChanel(ServiceChanel.COUNTER.getCode())
            .serviceCode("052002").serviceIndicator("").systemTrace("post create loan");

    public static final ParamsHeader PUT_CREATE_LOAN = ParamsHeader.paramsHeader()
            .serviceChanel(ServiceChanel.COUNTER.getCode())
            .serviceCode("052003").serviceIndicator("").systemTrace("put create loan");

    public static final ParamsHeader GET_CREATE_LOAN = ParamsHeader.paramsHeader()
            .serviceChanel(ServiceChanel.COUNTER.getCode())
            .serviceCode("052004").serviceIndicator("").systemTrace("get create loan");

    public static final ParamsHeader POST_INCREASE_LIMIT = ParamsHeader.paramsHeader()
            .serviceChanel(ServiceChanel.COUNTER.getCode())
            .serviceCode("052005").serviceIndicator("").systemTrace("post increase limit");

    public static final ParamsHeader PUT_INCREASE_LIMIT = ParamsHeader.paramsHeader()
            .serviceChanel(ServiceChanel.COUNTER.getCode())
            .serviceCode("052006").serviceIndicator("").systemTrace("put increase limit");

    public static final ParamsHeader POST_REDUCE_LIMIT = ParamsHeader.paramsHeader()
            .serviceChanel(ServiceChanel.COUNTER.getCode())
            .serviceCode("052007").serviceIndicator("").systemTrace("post reduce limit");

    public static final ParamsHeader PUT_REDUCE_LIMIT = ParamsHeader.paramsHeader()
            .serviceChanel(ServiceChanel.COUNTER.getCode())
            .serviceCode("052008").serviceIndicator("").systemTrace("put reduce limit");

    public static final ParamsHeader POST_PAY_LOAN = ParamsHeader.paramsHeader()
            .serviceChanel(ServiceChanel.COUNTER.getCode())
            .serviceCode("052009").serviceIndicator("").systemTrace("post pay loan");

    public static final ParamsHeader PUT_PAY_LOAN = ParamsHeader.paramsHeader()
            .serviceChanel(ServiceChanel.COUNTER.getCode())
            .serviceCode("052010").serviceIndicator("").systemTrace("put pay loan");

    public static final ParamsHeader POST_FINAL_LOAN = ParamsHeader.paramsHeader()
            .serviceChanel(ServiceChanel.COUNTER.getCode())
            .serviceCode("052011").serviceIndicator("").systemTrace("post final loan");

    public static final ParamsHeader PUT_FINAL_LOAN = ParamsHeader.paramsHeader()
            .serviceChanel(ServiceChanel.COUNTER.getCode())
            .serviceCode("052012").serviceIndicator("").systemTrace("put final loan");

    public static final ParamsHeader POST_HISTORY_LOAN = ParamsHeader.paramsHeader()
            .serviceChanel(ServiceChanel.COUNTER.getCode())
            .serviceCode("052013").serviceIndicator("").systemTrace("post history loan");

    public static final ParamsHeader GET_DETAIL_LOAN = ParamsHeader.paramsHeader()
            .serviceChanel(ServiceChanel.COUNTER.getCode())
            .serviceCode("052014").serviceIndicator("").systemTrace("get loan detail");

    private HeaderConstants() {
    }

    public static final ParamsHeader POST_KYC = ParamsHeader.paramsHeader()
            .serviceChanel(ServiceChanel.MOBILE.getCode())
            .serviceCode("031101").serviceIndicator("").systemTrace("post kyc");
}
