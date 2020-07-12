/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.controller.customer;

import com.viettel.arpu.constant.CommonConstant;
import com.viettel.arpu.model.entity.Customer;
import com.viettel.arpu.model.request.CustomerSearchForm;
import com.viettel.arpu.model.response.BaseResponse;
import com.viettel.arpu.model.response.CustomerResponse;
import com.viettel.arpu.service.customer.CustomerService;
import com.viettel.arpu.service.version.VersionService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

/**
 * Controller xử lý các request thuộc về customer trong whitelist
 *
 * @author trungnb3
 * @Date :5/21/2020, Thu
 */
@Controller
@RequestMapping(value = "api/customer")
@Slf4j
public class CustomerController {

    private CustomerService customerService;

    private VersionService versionService;

    @Autowired
    CustomerController(CustomerService customerService, VersionService versionService) {
        this.customerService = customerService;
        this.versionService = versionService;
    }

    /**
     *
     * lấy tất cả customer trong hệ thống với activeStatus = all
     *
     * @param customerSearchForm pojo file contains field request sent by client
     * @param pageable paginating object
     * @return CustomerResponse DTO object
     */
    @ApiOperation(value = "hiển thị whitelist",
            notes = "<b>lấy tất cả whitelist trong hệ thống và theo điều kiện tìm kiếm</b>")

    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "page number", defaultValue = "0"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "size of page", defaultValue = "20"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "sort by")
    })
    @GetMapping(value = "/search", params = "activeStatus=all")
    public ResponseEntity<CustomerResponse> all(@Valid CustomerSearchForm customerSearchForm,
                                                @ApiIgnore Pageable pageable) {
        Page<Customer> page = customerService.all(customerSearchForm, pageable);
        Long latestVersion = getLatestVersion();
        return ResponseEntity.ok(new CustomerResponse(page.map(customer ->
                                 customer.toDTO(latestVersion)), getLatestVersion()));
    }

    /**
     * lấy tất cả customer trong hệ thống với activeStatus = active
     *
     * @param customerSearchForm pojo file contains field request sent by client
     * @param pageable paginating object
     * @return CustomerResponse DTO object
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "page number", defaultValue = "0"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "size of page", defaultValue = "20"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "sort by")
    })
    @GetMapping(value = "/search", params = "activeStatus=" + Customer.ACTIVE)
    public ResponseEntity<CustomerResponse> active(@Valid CustomerSearchForm customerSearchForm,
                                                   @ApiIgnore Pageable pageable) {
        Long latestVersion = getLatestVersion();
        Page<Customer> page = customerService.active(customerSearchForm, latestVersion, pageable);
        return ResponseEntity.ok(new CustomerResponse(page.map(customer ->
                                                      customer.toDTO(latestVersion)), latestVersion));
    }

    /**
     *
     * lấy tất cả customer trong hệ thống với activeStatus = inactive
     *
     * @param customerSearchForm pojo file contains field request sent by client
     * @param pageable paginating object
     * @return CustomerResponse DTO object
     *
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "page number", defaultValue = "0"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "size of page", defaultValue = "20"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "sort by")
    })
    @GetMapping(value = "/search", params = "activeStatus=" + Customer.INACTIVE)
    public ResponseEntity<CustomerResponse> inactive(@Valid CustomerSearchForm customerSearchForm,
                                                     @ApiIgnore Pageable pageable) {
        Long latestVersion = getLatestVersion();
        Page<Customer> page = customerService.inactive(customerSearchForm, latestVersion, pageable);
        return ResponseEntity.ok(new CustomerResponse(page.map(customer ->
                                                      customer.toDTO(latestVersion)), latestVersion));
    }

    /**
     * Thực hiện khóa cho vay đối với 1 customer
     *
     * @param id customer id
     * @return BaseResponse<Customer> Ok response
     */
    @ApiOperation(value = "khóa vay trong whitelist",
            notes = "<b>khóa vay trong whitelist</b>")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "page number", defaultValue = "0"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "size of page", defaultValue = "20"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "sort by")
    })
    @PostMapping("/lock/{id}")
    public ResponseEntity<BaseResponse<Customer>> lock(@PathVariable("id") Long id) {
        customerService.lock(id);
        return ResponseEntity.ok(new BaseResponse<>());
    }

    /**
     * Thực hiện mở khóa cho vay tài khoản
     *
     * @param id customer id
     * @return BaseResponse<Customer> Ok response
     */
    @ApiOperation(value = "Mở khóa vay trong whitelist",
            notes = "<b>khóa vay trong whitelist</b>")
    @PostMapping("/unlock/{id}")
    public ResponseEntity<BaseResponse<Customer>> unlock(@PathVariable("id") Long id) {
        customerService.unlock(id);
        return ResponseEntity.ok(new BaseResponse<>());
    }

    /**
     * Hàm tiện ích lấy version mới nhất của whitelist
     * @return version
     */
    private Long getLatestVersion() {
        return versionService.getLatestVersionForBatchId(CommonConstant.BATCH_WHITELIST_ID);
    }
}
