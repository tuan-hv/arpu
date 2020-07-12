/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.service.sftp.validator;

import com.viettel.arpu.constant.BatchConstant;
import com.viettel.arpu.model.entity.Customer;
import com.viettel.arpu.model.entity.Loan;
import com.viettel.arpu.model.entity.Version;
import com.viettel.arpu.repository.LoanRepository;
import com.viettel.arpu.service.sftp.auditlog.SyncAuditLogService;
import com.viettel.arpu.service.sftp.form.PayedLoanForm;
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
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Class service validate tất toán khoản vay
 *
 * @author tuongvx
 */
@Slf4j
@Service(value = "syncPLValidatorServiceImpl")
public class PaidLoanValidatorServiceImpl implements ValidatorService {
    private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private int countRecord = 0;
    private StringBuilder stringBuilder;
    private Boolean flag = true;

    private SyncAuditLogService syncAuditLogService;
    private LoanRepository loanRepository;
    @Qualifier("validatorMessageSource")
    private MessageSource messageSource;

    private static final int RAWDATA_IDX = 3;


    @Autowired
    public PaidLoanValidatorServiceImpl(SyncAuditLogService syncAuditLogService, MessageSource messageSource, LoanRepository loanRepository) {
        this.syncAuditLogService = syncAuditLogService;
        this.messageSource = messageSource;
        this.loanRepository = loanRepository;
    }

    /**
     * Validate dành cho đồng bộ tất toán khoản vay
     *
     * @return
     */
    @Override
    public List<String> validatePayLoanList(List<String> rawData, Version version) {
        final Validator validator = factory.getValidator();
        List<String> numberAccList = new ArrayList<>();
        rawData.stream().filter(Objects::nonNull).forEach((String it) -> {
            countRecord++;
            flag = true;
            stringBuilder = new StringBuilder();
            PayedLoanForm payedLoanForm = Optional.ofNullable(extractObject(it, validator)).orElse(new PayedLoanForm());
            if (Boolean.TRUE.equals(flag)) {
                numberAccList.add(payedLoanForm.getNumberAccount());
            } else {
                try {
                    log.info("Save record payLoan error to AuditLog");
                    syncAuditLogService.saveAuditLog(version.getId(), stringBuilder.toString(), Long.valueOf(countRecord), it);
                } catch (Exception ex) {
                    log.error("Auditlog for sync payLoan error with: " + ex);
                }
            }
        });
        countRecord = 0;
        return numberAccList.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }


    /***
     * @param rawData
     * @param validator
     * @return
     */
    private PayedLoanForm extractObject(String rawData, Validator validator) {
        PayedLoanForm row = null;
        try {
            boolean isValid = rawData.split("#").length > RAWDATA_IDX;
            if (Boolean.FALSE.equals(isValid)) {
                flag = false;
                stringBuilder.append(messageSource.getMessage(BatchConstant.ERROR_SYNC_PAY_LOAN_LENGTH_FILE, null, new Locale("vi")));
            } else {
                String[] str = rawData.split("#".trim());
                List<String> lst = Arrays.asList(str);
                row = PayedLoanForm.builder()
                        .numberAccount(lst.get(RAWDATA_IDX))
                        .build();
                validFiledDetail(row, validator);
            }
        } catch (Exception e) {
            log.error("Error extractObject : " + e);
        }
        return row;
    }


    /**
     * Validate từng trường của một bản ghi tất toán khoản vay ( một hàng)
     * Nếu có lỗi sẽ append vào một buffer
     * Trả về True nếu bản ghi thỏa mãn valid và FALSE khi ngược lại
     *
     * @param opt
     * @param validator
     * @return
     */
    private void validFiledDetail(PayedLoanForm opt, Validator validator) {
        Set<? extends ConstraintViolation<?>> constraintViolations = validator.validate(opt);
        boolean isCheckedValidate = !constraintViolations.isEmpty();
        if (Boolean.TRUE.equals(isCheckedValidate)) {
            flag = false;
            constraintViolations.forEach((ConstraintViolation<?> item) -> {
                stringBuilder.append(messageSource.getMessage(item.getMessage(), null
                        , new Locale("vi")));
            });
        } else {
            Optional<Loan> optionalLoan = loanRepository.findFirstByLoanAccount(opt.getNumberAccount());
            if (!optionalLoan.isPresent()) {
                flag = false;
                stringBuilder.append(messageSource.getMessage(BatchConstant.SYNC_LOAN_ACCOUNT_NOT_FOUND, null
                        , new Locale("vi")));
            }
        }
    }

    @Override
    public List<Customer> validateWhiteListData(List<String> list, Version version) {
        return new ArrayList<>();
    }
}
