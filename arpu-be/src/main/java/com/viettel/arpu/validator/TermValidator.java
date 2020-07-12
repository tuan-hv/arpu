/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.arpu.validator;

import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * @Author VuHQ
 * @Since 6/24/2020
 */
@NoArgsConstructor
public class TermValidator implements ConstraintValidator<Term, String> {
   private final String term = "1 2 3 6";
   private static final Pattern termRegex = Pattern.compile("^[0-9]*$");
   @Override
   public void initialize(Term constraint) {
      //Do nothing
   }

   public boolean isValid(String obj, ConstraintValidatorContext context) {
      if (StringUtils.isEmpty(obj)) {
         return true;
      }
      return termRegex.matcher(obj).matches() && term.contains(obj.trim());
   }
}
