/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.model.entity;

import com.viettel.arpu.constant.enums.Gender;
import com.viettel.arpu.locale.Translator;
import com.viettel.arpu.model.dto.CustomerDTO;
import com.viettel.arpu.utils.BatchStatus;
import com.viettel.arpu.utils.ObjectMapperUtils;
import lombok.*;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * customer entity
 */
@Entity(name = "Customer")
@Table(name = "tbl_customer", uniqueConstraints = {
        @UniqueConstraint(columnNames = "msisdn"),
        @UniqueConstraint(columnNames = "identityNumber"),
})
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class Customer extends AbstractAuditingEntity {
    private static final long serialVersionUID = -5231812722728192949L;
    /**
     * trạng thái không hoạt động
     */
    public static final String INACTIVE = "inactive";
    /**
     * trạng thái đang hoạt động
     */
    public static final String ACTIVE = "active";

    /**
     * trạng thái khóa
     * khóa/mở khóa
     */
    public enum LOCK_STATUS {
        LOCK, UNLOCK
    }

    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    /**
     * số điện thoại
     */
    private String msisdn;

    /**
     * tên đầy đủ
     */
    private String fullName;

    /**
     * ngày sinh
     */
    private LocalDate dateOfBirth;

    /**
     * giới tính
     */
    @Enumerated(EnumType.STRING)
    private Gender gender;

    /**
     * loại định danh (CMTND/Thẻ căn cước)
     */
    private String identityType;
    /**
     * số CMTND/ số căn cước
     */
    private String identityNumber;
    /**
     * ngày cấp
     */
    private LocalDate dateOfIssue;
    /**
     * nơi cấp
     */
    private String placeOfIssue;
    /**
     * mức vay thấp nhất
     */
    private BigDecimal loanMinimum;
    /**
     * mức vay cao nhất
     */
    private BigDecimal loanMaximum;
    /**
     * điểm arpu
     */
    private Long arpu;
    /**
     * điểm tối thiểu
     */
    private Integer scoreMin;
    /**
     * điểm tối đa
     */
    private Integer scoreMax;
    /**
     * điểm trung bình arpu 3 tháng gần nhất
     */
    private BigDecimal arpuLatestThreeMonths;

    /**
     * trạng thái khóa
     */
    @Enumerated(value = EnumType.STRING)
    private LOCK_STATUS lockStatus;

    /**
     * danh sách người tham chiếu
     */
    @OneToMany(mappedBy = "customer")
    private Set<CustomerRef> customerRef = new HashSet<>();

    /**
     * danh sách khoản vay
     */
    @OneToMany(mappedBy = "customer")
    private Set<Loan> loans = new HashSet<>();

    /**
     * trạng thái đồng bộ dữ liệu MB
     * insert/update/delete
     */
    @Enumerated(EnumType.STRING)
    private BatchStatus batchStatus;

    /**
     * địa chỉ
     */
    @OneToOne(cascade = CascadeType.ALL)
    private Address address;

    /**
     * version
     */
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "versionId", referencedColumnName = "id"),
    })
    private Version version;
    /**
     * quốc tịch
     */
    private String nationality;
    /**
     * customerAccount
     */
    private String customerAccount;
    /**
     * tài khoản ví viettel
     */
    private String viettelpayWallet;

    /**
     * email
     */
    private String email;

    public CustomerDTO toDTO(Long latestVersion) {
        CustomerDTO customerDTO = ObjectMapperUtils.map(this, CustomerDTO.class);

        String lockStatusCustomer = Optional.ofNullable(getLockStatus())
                .map(Enum::name)
                .orElse(LOCK_STATUS.UNLOCK.name()).toLowerCase();

        customerDTO.setLockStatusId(lockStatusCustomer);
        customerDTO.setLockStatus(Translator.toLocale(lockStatusCustomer));

        String status = ACTIVE;
        if (getVersion().getVersionSync() < latestVersion) {
            status = INACTIVE;
        }
        customerDTO.setActiveStatus(Translator.toLocale(status));
        customerDTO.setGender(Translator.toLocale(gender.name()));
        return customerDTO;
    }
}
