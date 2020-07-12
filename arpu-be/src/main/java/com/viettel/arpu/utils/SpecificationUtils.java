/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class SpecificationUtils {

    private SpecificationUtils() {
    }

    /**
     * @description tạo câu truy vấn tìm kiếm giá trị lớn hơn hoặc bằng giá trị nhập vào
     * @param value
     * @param column
     * @param <T>
     * @return Specification
     */
    public static <T> Specification<T> greaterThanOrEqualTo(String value, String column) {
        if (StringUtils.isEmpty(value)) {
            return null;
        } else {
            return ((Root<T> root, CriteriaQuery<?> cq, CriteriaBuilder cb) ->
                    cb.greaterThanOrEqualTo(root.get(column), value.trim()));
        }
    }

    /**
     * @description tạo câu truy vấn tìm kiếm giá trị nhỏ hơn hoặc bằng giá trị nhập vào
     * @param value
     * @param column
     * @param <T>
     * @return Specification
     */
    public static <T> Specification<T> lessThanOrEqualTo(String value, String column) {
        if (StringUtils.isEmpty(value)) {
            return null;
        } else {
            return ((Root<T> root, CriteriaQuery<?> cq, CriteriaBuilder cb) ->
                    cb.lessThanOrEqualTo(root.get(column), value.trim()));
        }
    }

    /**
     * @description tạo câu truy vấn tìm kiếm ngày tạo lớn hơn ngày nhập vào
     * @param value
     * @param column
     * @param <T>
     * @return Specification
     */
    public static <T> Specification<T> greaterThanOrEqualFromDateInstant(LocalDate value, String column) {
        if (value == null) {
            return null;
        } else {
            return ((Root<T> root, CriteriaQuery<?> cq, CriteriaBuilder cb) ->
                    cb.greaterThanOrEqualTo(root.get(column), value.atTime(LocalTime.MIN)));
        }
    }

    /**
     * @description tạo câu truy vấn tìm kiếm ngày tạo nhỏ hơn ngày nhập vào
     * @param value
     * @param column
     * @param <T>
     * @return Specification
     */
    public static <T> Specification<T> lessThanOrEqualToDateInstant(LocalDate value, String column) {
        if (value == null) {
            return null;
        }
        else {
            return ((Root<T> root, CriteriaQuery<?> cq, CriteriaBuilder cb) ->
                    cb.lessThanOrEqualTo(root.get(column), value.atTime(LocalTime.MAX)));
        }
    }

    /**
     * @description tạo câu truy vấn tìm kiếm giá trị bằng với giá trị đầu vào
     * @param value
     * @param column
     * @param <T>
     * @return Specification
     */
    public static <T> Specification<T> equalParam(String value, String column) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        else {
            return ((Root<T> root, CriteriaQuery<?> cq, CriteriaBuilder cb) ->
                    cb.equal(root.get(column), value.trim()));
        }
    }

    /**
     * @description tạo câu truy vấn tìm kiếm dữ liệu với danh sách giá trị cho trước
     * @param values
     * @param column
     * @param <T>
     * @return Specification
     */
    public static <T> Specification<T> hasListParam(List<?> values, String column) {
        if (values == null || values.isEmpty()) {
            return null;
        }
        else {
            return ((Root<T> root, CriteriaQuery<?> cq, CriteriaBuilder cb) ->
                    root.get(column).in(values));
        }
    }

}
