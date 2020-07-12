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
import java.math.BigDecimal;

/**
 * @Author VuHQ
 * @Since 6/16/2020
 */
@NoArgsConstructor
@Slf4j
public class LoanLimitValidator implements ConstraintValidator<LoanLimit, Object> {
   private String minLimit;
   private String maxLimit;
   private String amount;
   @Override
   public void initialize(LoanLimit constraint) {
      minLimit = constraint.minLimit();
      maxLimit = constraint.maxLimit();
      amount = constraint.amount();
   }

   public boolean isValid(final Object value, final ConstraintValidatorContext context) {
      try {
         final BigDecimal minLimitValue = new BigDecimal(BeanUtils.getProperty(value,minLimit));
         final BigDecimal maxLimitValue = new BigDecimal(BeanUtils.getProperty(value, maxLimit));
         final BigDecimal amountValue = new BigDecimal(BeanUtils.getProperty(value, amount));

         if ( minLimitValue.compareTo(amountValue) >= 0 && amountValue.compareTo(maxLimitValue) <= 0) {
            return true;
         }
         return false;
      } catch (Exception e) {
         log.error("Error validate: " + e);
         return true;
      }
   }
}
