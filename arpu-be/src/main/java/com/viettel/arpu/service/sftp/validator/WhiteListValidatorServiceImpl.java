/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.service.sftp.validator;

import com.viettel.arpu.constant.BatchConstant;
import com.viettel.arpu.model.entity.Customer;
import com.viettel.arpu.model.entity.Version;
import com.viettel.arpu.repository.CustomerRepository;
import com.viettel.arpu.service.sftp.auditlog.SyncAuditLogService;
import com.viettel.arpu.service.sftp.form.CustomerForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Validate đồng bộ danh sách whitelist
 *
 * @author tuongvx
 */
@Slf4j
@Service("syncWLValidatorServiceImpl")
public class WhiteListValidatorServiceImpl implements ValidatorService {
    private int countRecord;
    private StringBuilder stringBuilder;
    private Boolean flag = true;
    private static final int RAWDATA_LENGTH = 11;
    private static final int IDX_0 = 0;
    private static final int IDX_1 = 1;
    private static final int IDX_2 = 2;
    private static final int IDX_3 = 3;
    private static final int IDX_4 = 4;
    private static final int IDX_5 = 5;
    private static final int IDX_6 = 6;
    private static final int IDX_7 = 7;
    private static final int IDX_8 = 8;
    private static final int IDX_9 = 9;
    private static final int IDX_10 = 10;
    private static final int IDX_11 = 11;
    private static final long COUNT_CUSTOMER = 2l;
    private SyncAuditLogService syncAuditLogService;
    private CustomerRepository customerRepository;
    @Qualifier("validatorMessageSource")
    private MessageSource messageSource;

    @Autowired
    public WhiteListValidatorServiceImpl(SyncAuditLogService syncAuditLogService, MessageSource messageSource,
                                         CustomerRepository customerRepository) {
        this.syncAuditLogService = syncAuditLogService;
        this.messageSource = messageSource;
        this.customerRepository = customerRepository;
    }


    /**
     * Validate dành cho đồng bộ whiteList
     *
     * @param rawData
     * @param version
     * @return
     */
    @Override
    public List<Customer> validateWhiteListData(List<String> rawData, Version version) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        final Validator validator = factory.getValidator();
        List<Customer> customers = new ArrayList<>();
        rawData.stream().filter(Objects::nonNull).forEach((String it) -> {
            countRecord++;
            flag = true;
            stringBuilder = new StringBuilder();
            CustomerForm customerForm = extractObject(it, validator);
            if (Boolean.TRUE.equals(flag)) {
                Customer customer = CustomerForm.from(customerForm);
                customers.add(customer);
            } else {
                try {
                    log.info("Save record payLoan error to AuditLog");
                    syncAuditLogService.saveAuditLog(version.getId(), stringBuilder.toString(),
                                                     Long.valueOf(countRecord), it);
                } catch (Exception ex) {
                    log.error("Auditlog for sync payLoan error with: {}", ex);
                }
            }
        });
        countRecord = 0;
        return customers.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }


    /***
     * @param rawData
     * @param validator
     * @return
     */
    private CustomerForm extractObject(String rawData, Validator validator) {
        CustomerForm row = null;
        boolean isValid = rawData.split(";").length > RAWDATA_LENGTH;
        try {
            if (Boolean.FALSE.equals(isValid)) {
                flag = false;
                stringBuilder.append(messageSource.getMessage(BatchConstant.ERROR_SYNC_WHITE_LIST_LENGTH_FILE,
                              null, new Locale("vi")));
            } else {
                String[] str = rawData.split(";".trim());
                List<String> lst = Arrays.asList(str);
                row = CustomerForm.builder()
                        .msisdn(lst.get(IDX_0).trim())
                        .fullName(lst.get(IDX_1).trim())
                        .dateOfBirth(lst.get(IDX_2).trim())
                        .gender(lst.get(IDX_3).trim())
                        .identityType(lst.get(IDX_4).trim())
                        .identityNumber(lst.get(IDX_5).trim())
                        .dateOfIssue(lst.get(IDX_6).trim())
                        .placeOfIssue(lst.get(IDX_7).trim())
                        .viettelpayWallet(lst.get(IDX_8).trim())
                        .arpuLatestThreeMonths(lst.get(IDX_9).trim())
                        .scoreMin(lst.get(IDX_10).trim())
                        .scoreMax(lst.get(IDX_11).trim())
                        .build();
                validFiledDetail(row, validator);
            }
        } catch (Exception e) {
            log.error("Error extractObject : " + e);
        }
        return row;
    }

    /**
     * Validate từng trường của một bản ghi whitelist( một hàng)
     *
     * @param opt
     * @param validator
     * @return
     */
    private void validFiledDetail(CustomerForm opt, Validator validator) {
        Set<? extends ConstraintViolation<?>> constraintViolations = validator.validate(opt);
        boolean isCheckedValidate = !constraintViolations.isEmpty();
        if (Boolean.TRUE.equals(isCheckedValidate)) {
            flag = false;
            constraintViolations.stream().filter(Objects::nonNull).forEach((ConstraintViolation<?> item) -> {
                stringBuilder.append(messageSource.getMessage(item.getMessage(), null
                        , new Locale("vi")));
            });
        } else {
            if (customerRepository.getCountCustomer(opt.getMsisdn(), opt.getIdentityNumber()) >= COUNT_CUSTOMER) {
                flag = false;
                stringBuilder.append(messageSource.getMessage(BatchConstant.WHITE_LIST_DATA_IS_UNI, null
                        , new Locale("vi")));
            }
        }
    }

    @Override
    public List<String> validatePayLoanList(List<String> list, Version version) {
        return new ArrayList<>();
    }
}
