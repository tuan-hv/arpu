/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.validator;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Author tuongvx
 * @Since 6/17/2020
 */
@NoArgsConstructor
@Slf4j
public class ScoreMatchValidator implements ConstraintValidator<ScoreMatch, Object> {
    private String scoreMin;
    private String scoreMax;

    @Override
    public void initialize(ScoreMatch constraint) {
        scoreMin = constraint.scoreMin();
        scoreMax = constraint.scoreMax();
    }

    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        try {
            final Integer firstValue = Integer.valueOf(BeanUtils.getProperty(value, scoreMin));
            final Integer secondValue = Integer.valueOf(BeanUtils.getProperty(value, scoreMax));
            if (firstValue > secondValue) {
                return false;
            }
            return true;
        } catch (Exception e) {
            log.error("Error validate: " + e);
            return true;
        }
    }
}
