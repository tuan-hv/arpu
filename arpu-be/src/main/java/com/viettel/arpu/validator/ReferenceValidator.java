/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.validator;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Author VuHQ
 * @Since 6/22/2020
 */
@NoArgsConstructor
@Slf4j
public class ReferenceValidator implements ConstraintValidator<Reference, Object> {
    private String referencerName;
    private String referencerType;
    private String referencerMobile;
    private String referencerEmail;

    @Override
    public void initialize(Reference constraintAnnotation) {
        referencerName = constraintAnnotation.referencerName();
        referencerType = constraintAnnotation.referencerType();
        referencerMobile = constraintAnnotation.referencerMobile();
        referencerEmail = constraintAnnotation.referencerEmail();

    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            final String referencerNameValue = BeanUtils.getProperty(value, referencerName);
            final String referencerTypeValue = BeanUtils.getProperty(value, referencerType);
            final String referencerMobileValue = BeanUtils.getProperty(value, referencerMobile);
            final String referencerEmailValue = BeanUtils.getProperty(value, referencerEmail);

            boolean isAllEmpty = StringUtils.isEmpty(referencerNameValue) &&
                    StringUtils.isEmpty(referencerTypeValue) &&
                    StringUtils.isEmpty(referencerMobileValue) &&
                    StringUtils.isEmpty(referencerEmailValue);
            boolean isAllNotEmpty = !StringUtils.isEmpty(referencerNameValue) &&
                    !StringUtils.isEmpty(referencerTypeValue) &&
                    !StringUtils.isEmpty(referencerMobileValue) &&
                    !StringUtils.isEmpty(referencerEmailValue);
            if (isAllEmpty || isAllNotEmpty) {
                return true;
            }

        } catch (Exception e) {
            log.error("Error validate: " + e);
            return true;
        }
        return false;
    }
}
