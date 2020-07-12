/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.service.mb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.arpu.config.MbStorageProperties;
import com.viettel.arpu.constant.HeaderConstants;
import com.viettel.arpu.constant.ParamsHeader;
import com.viettel.arpu.model.request.mb.MbConfirmCreateLoanForm;
import com.viettel.arpu.model.response.mb.MbBaseResponse;
import com.viettel.arpu.utils.GenerateRequestIdUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import javax.print.DocFlavor;
import javax.validation.Valid;
import java.util.Map;

/**
 * @Author VuHQ
 * @Since 6/5/2020
 */
@Slf4j
public abstract class AbstractMbService {
    @Autowired
    private MbStorageProperties mbStorageProperties;

    @Autowired
    @Qualifier("mbRestTemplate")
    private RestTemplate mbRestTemplate;
    /**
     * @description gọi api từ mb với requestId tự tạo ra
     * @param form
     * @param httpMethod
     * @param url
     * @param mbClass
     * @param headersValue
     * @param <T>
     * @return
     */
    protected <T extends MbBaseResponse> T createLoanSendToMbAPI(@Valid MbConfirmCreateLoanForm form, HttpMethod httpMethod, String url
            , Class<T> mbClass, ParamsHeader headersValue, String requestId) {

        HttpHeaders header = headersValue.setRequestId(requestId).build();
        System.out.println("requestId: "+requestId);
        try {
            return mbRestTemplate.exchange(url
                    , httpMethod, new HttpEntity<>(form,header)
                    , mbClass).getBody();

        }catch (HttpStatusCodeException e) {
            log.error("Error createLoanSendToMbAPI and go to check if loan existed: " + e);
            StringBuilder urlGet = new StringBuilder(mbStorageProperties.getCreateloan());
            urlGet.append("?sourceMobile=");
            urlGet.append(form.getSourceMobile());
            urlGet.append("&sourceNumber=");
            urlGet.append(form.getSourceNumber());
            urlGet.append("&loanRequestId=");
            urlGet.append(form.getLoanRequestId());
            return sendToMbAPI(form, HttpMethod.GET, mbStorageProperties.getCreateloan()
                    , mbClass, HeaderConstants.GET_CREATE_LOAN, GenerateRequestIdUtils.generateRequestId());
        }
    }

    /**
     * * @description gọi api từ mb với requestId được truyền vào
     * @param form
     * @param httpMethod
     * @param url
     * @param mbClass
     * @param headersValue
     * @param requestId
     * @param <T>
     * @return
     */
    protected <T extends MbBaseResponse> T sendToMbAPI(Object form, HttpMethod httpMethod, String url
            , Class<T> mbClass, ParamsHeader headersValue, String requestId) {

        HttpHeaders header = headersValue.setRequestId(requestId).build();
        System.out.println("requestId: "+requestId);
        try {
            return mbRestTemplate.exchange(url
                    , httpMethod, new HttpEntity<>(form,header)
                    , mbClass).getBody();

        } catch(HttpStatusCodeException e) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                T t = objectMapper.readValue(e.getResponseBodyAsString(), mbClass);
                t.setErrorData(e.getResponseBodyAsString());
                return t;
            } catch (Exception ex) {
                log.error("Error sendToMbAPI: " + ex);
                throw e;
            }
        }
    }

}
