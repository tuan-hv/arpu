/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.controller.loan;

import com.viettel.arpu.model.dto.LoanDTO;
import com.viettel.arpu.model.request.SearchLoanForm;
import com.viettel.arpu.model.response.PageResponse;
import com.viettel.arpu.service.loan.LoanService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

/**
 * @Author VuHQ
 * @Since 5/25/2020
 */
@RestController
@RequestMapping("api/loans")
public class LoanController {
    @Autowired
    private LoanService loanService;

    @ApiOperation(value = "tìm kiếm trên màn hình danh sách khoản vay"
            ,  notes = "<b>lấy tất cả các các hồ sơ vay theo các param truyền vào.</b>")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "page number", defaultValue = "0"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "size of page", defaultValue = "20"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "sort by")
    })


    @GetMapping("/search")
    public ResponseEntity<PageResponse<Page<LoanDTO>>> searchLoan(@Valid SearchLoanForm searchLoanForm,
                                                                  @ApiIgnore Pageable pageable){
        return ResponseEntity.ok(new PageResponse<>(loanService.searchLoanByForm(searchLoanForm, pageable)));
    }

}
