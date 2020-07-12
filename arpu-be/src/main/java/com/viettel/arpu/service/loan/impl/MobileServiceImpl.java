/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.service.loan.impl;

import com.viettel.arpu.constant.CommonConstant;
import com.viettel.arpu.constant.IsoConstant;
import com.viettel.arpu.constant.MBConstant;
import com.viettel.arpu.constant.VersionConstant;
import com.viettel.arpu.exception.MbNotFoundException;
import com.viettel.arpu.exception.MbOtpFailException;
import com.viettel.arpu.exception.WithdrawMoneyException;
import com.viettel.arpu.locale.Translator;
import com.viettel.arpu.model.dto.ReferenceDTO;
import com.viettel.arpu.model.dto.WithdrawMoneyDTO;
import com.viettel.arpu.model.dto.mb.MbCustomerDTO;
import com.viettel.arpu.model.dto.mb.MbCustomerInfoDTO;
import com.viettel.arpu.model.dto.mb.MbExistPhoneNumberDTO;
import com.viettel.arpu.model.dto.mb.MbListLoanDTO;
import com.viettel.arpu.model.dto.mb.MbLoanLimitDTO;
import com.viettel.arpu.model.entity.Address;
import com.viettel.arpu.model.entity.Customer;
import com.viettel.arpu.model.entity.CustomerRef;
import com.viettel.arpu.model.entity.Loan;
import com.viettel.arpu.model.request.CustomerSearchForm;
import com.viettel.arpu.model.request.WithdrawMoneyForm;
import com.viettel.arpu.model.request.mb.MbCheckCustomerForm;
import com.viettel.arpu.repository.CustomerRepository;
import com.viettel.arpu.repository.LoanRepository;
import com.viettel.arpu.repository.VersionRepository;
import com.viettel.arpu.repository.specifications.CustomerSpecifications;
import com.viettel.arpu.repository.specifications.MbSpecifications;
import com.viettel.arpu.service.loan.MobileService;
import com.viettel.arpu.utils.ObjectMapperUtils;
import com.viettel.socket.netty.beans.CoreFieldMap;
import com.viettel.socket.netty.beans.IsoObject;
import com.viettel.socket.netty.connector.NettyConnector;
import lombok.extern.slf4j.Slf4j;
import org.jpos.iso.ISOException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author VuHQ
 * @Since 6/5/2020
 */
@Service
@Slf4j
public class MobileServiceImpl implements MobileService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private VersionRepository versionRepository;

    @Autowired
    private LoanRepository loanRepository;

    /**
     * @description kiểm tra xem khách hàng có thỏa mãn whitelist không: tồn tại, ở trạng thái đang hoạt đọng
     * và không bị khóa
     * @param mbCheckCustomerForm
     * @return
     */
    @Override
    public MbExistPhoneNumberDTO isExistCustomer(@Valid MbCheckCustomerForm mbCheckCustomerForm) {
        Optional<Customer> result = customerRepository.findOne(CustomerSpecifications
                .spec()
                .msisdn(mbCheckCustomerForm.getPhoneNumber())
                .active(versionRepository.getLatestVersionForBatchId(VersionConstant.BATCH_WHITELIST_ID))
                .lock(CustomerSearchForm.LOCK_REQUEST.UNLOCK)
                .build());
        return new MbExistPhoneNumberDTO(result.isPresent());
    }

    /**
     * @description lấy thông tin khách hàng, có kèm thông tin người tham chiếu
     * @param mbCheckCustomerForm
     * @return
     */
    @Override
    public MbCustomerDTO getCustomer(@Valid MbCheckCustomerForm mbCheckCustomerForm) {
        Customer customer = getCustomerByMsisdn(mbCheckCustomerForm.getPhoneNumber());
        Set<ReferenceDTO> referenceDTOs = new HashSet<>();
        ReferenceDTO referenceDTO;
        MbCustomerDTO mbCustomerDTO = new MbCustomerDTO();

        mbCustomerDTO.setMbCustomerInfoDTO(toMbCustomerDTO(customer));

        for (CustomerRef customerRefElement: customer.getCustomerRef()) {
            referenceDTO = ObjectMapperUtils.map(customerRefElement.getReference(), ReferenceDTO.class);
            referenceDTO.setRelationship(Optional.ofNullable(customerRefElement.getRelationship())
                    .flatMap(codeCode -> Optional.ofNullable(codeCode.getCodeName()))
                    .orElseThrow(() -> new MbNotFoundException("error.msg.loan.status.notfound")));
            referenceDTOs.add(referenceDTO);
        }

        mbCustomerDTO.setListReference(referenceDTOs);
         return mbCustomerDTO;
    }

    /**
     * @description lấy thông tin các khoản vay của khách hàng
     * @param mbCheckCustomerForm
     * @return
     */
    @Override
    public MbListLoanDTO getLoansByPhone(@Valid MbCheckCustomerForm mbCheckCustomerForm) {
        List<Loan> results = loanRepository.findAll(
                             MbSpecifications.hasPhoneNumber(mbCheckCustomerForm.getPhoneNumber()));
        MbListLoanDTO mbListLoanDTO = new MbListLoanDTO();

        if (CollectionUtils.isEmpty(results)) {
            return mbListLoanDTO;
        }

        Loan lastElement = results.get(0);
        mbListLoanDTO.setTotalElement(String.valueOf(results.size()));
        mbListLoanDTO.setLoans(results.stream().map(Loan::toLoanDTO).collect(Collectors.toList()));
        mbListLoanDTO.setLastStatus(Optional.ofNullable(lastElement.getLoanStatus())
                .flatMap(codeCode -> Optional.of(codeCode.getId()))
                .orElseThrow(() -> new MbNotFoundException("error.msg.loan.status.notfound")));
        return mbListLoanDTO;
    }

    /**
     * @description lấy thông tin khách hàng bằng số điện thoại
     * @param phoneNumber
     * @return
     */
    private Customer getCustomerByMsisdn(String phoneNumber) {
        return customerRepository.findByMsisdn(phoneNumber).orElseThrow(
                () -> new MbNotFoundException("error.msg.customer.not.found"));
    }

    /**
     * @description chuyển đổi thông tin khách hàng sang dto
     * @param customer
     * @return
     */
    private MbCustomerInfoDTO toMbCustomerDTO(Customer customer) {
        MbCustomerInfoDTO mbCustomerDTO = ObjectMapperUtils.map(customer, MbCustomerInfoDTO.class);
        mbCustomerDTO.setGender(Translator.toLocale(customer.getGender().toString()));
        Address address = customer.getAddress();
        if (address != null) {
            BeanUtils.copyProperties(address, mbCustomerDTO);
        }
        return mbCustomerDTO;
    }

    /**
     * @description lấy thông tin hạn mức  của khách hàng
     * @param mbCheckCustomerForm
     * @return
     */
    @Override
    public MbLoanLimitDTO getCustomerLimit(@Valid MbCheckCustomerForm mbCheckCustomerForm) {
            Customer customer = getCustomerByMsisdn(mbCheckCustomerForm.getPhoneNumber());
            return MbLoanLimitDTO.builder().maxAmount(Optional.ofNullable(customer.getLoanMaximum())
                            .map(bigDecimal -> String.valueOf(bigDecimal.intValue()))
                            .orElse(CommonConstant.DEFAULT_LIMIT))
                    .minAmount(Optional.ofNullable(customer.getLoanMinimum())
                            .map(bigDecimal -> String.valueOf(bigDecimal.intValue()))
                            .orElse(CommonConstant.DEFAULT_LIMIT))
                    .currentLimit(CommonConstant.DEFAULT_LIMIT)
                    .build();
    }
    
    /**
     * Gửi bản tin giao dịch rút tiền MB và nhận response trả về
     *
     * @param withdrawMoneyForm
     * @return WithdrawMoneyDTO
     */
    public WithdrawMoneyDTO withdrawMoneyToVTP(@Valid WithdrawMoneyForm withdrawMoneyForm)
                           throws WithdrawMoneyException{
        WithdrawMoneyDTO withdrawMoneyDTO = null;
        String responseCode = IsoConstant.RESPONSE_CODE;
        boolean hasResponse;
        try {
            // Gui ban tin iso
            NettyConnector connector = NettyConnector.getInstance();
            IsoObject res = connector.requestWithHystrix(createMBMessage(withdrawMoneyForm),
                    IsoConstant.CONN_NAME);
            hasResponse = res.isResponse();
            if (hasResponse && res.hasField(CoreFieldMap.RESPONSE_CODE.getFldNo())) {// ISO tra ve co response code
                responseCode = res.getValue(CoreFieldMap.RESPONSE_CODE.getFldNo()).toString();
            }
            // Rut tien thanh cong
            if (MBConstant.SUCCESS.equals(responseCode)) {
                withdrawMoneyDTO = buildWithdrawMoneyDTO(res);
            }
        } catch (Exception ex) {
            log.error("Loi rut tien tu MB ve VTP: " + ex);
            throw new WithdrawMoneyException();
        }

        if (withdrawMoneyDTO == null) {
            // Sai OTP
            if (IsoConstant.WRONG_OTP_CODE.equals(responseCode)) {
                throw new MbOtpFailException();
            } else {
                throw new WithdrawMoneyException();
            }
        }

        return withdrawMoneyDTO;
    }

    private IsoObject createMBMessage(WithdrawMoneyForm withdrawMoneyForm) throws ISOException {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatterTransDate = DateTimeFormatter.ofPattern(IsoConstant.TRANS_DATE_FORMAT);
        DateTimeFormatter formatterRefNumber = DateTimeFormatter.ofPattern(IsoConstant.REF_NUMBER_FORMAT);
        DateTimeFormatter formatterRequestId = DateTimeFormatter.ofPattern(IsoConstant.REQUEST_ID_FORMAT);

        IsoObject obj = new IsoObject();
        obj.setMTI(IsoConstant.MTI);
        obj.setProcessCode(IsoConstant.PROCESS_CODE);
        obj.setTransAmount(createAmount(withdrawMoneyForm.getAmount()));
        obj.setTransDate(now.format(formatterTransDate));
        obj.setSystemTrace(IsoConstant.SYSTEM_TRACE);
        obj.setClientId(IsoConstant.CLIENT_ID);
        obj.setMoneySource(IsoConstant.MONEY_SOURCE);
        obj.setReferenceNumber(now.format(formatterRefNumber) + obj.getSystemTrace());
        obj.setReqId(now.format(formatterRequestId));
        obj.setMobileNumber(withdrawMoneyForm.getSourceMobile());
        obj.setMAC(withdrawMoneyForm.getSourceMobile());
        obj.setTransType(IsoConstant.TRANS_TYPE);
        obj.setBankCode(IsoConstant.BANK_CODE);
        obj.setAccountNumber(withdrawMoneyForm.getBankAccount());
        obj.setOtp(withdrawMoneyForm.getOtp());

        return obj;
    }

    private String createAmount(String amount) {
        int length = amount.length();
        if (length < IsoConstant.AMOUNT_LENGTH) {
            int remain = IsoConstant.AMOUNT_LENGTH - length;
            for (int i = 0; i < remain; i++) {
                amount = "0" + amount;
            }
        }
        return amount;
    }

    private WithdrawMoneyDTO buildWithdrawMoneyDTO(IsoObject isoObject) throws ISOException {
        WithdrawMoneyDTO withdrawMoneyDTO = new WithdrawMoneyDTO();
        withdrawMoneyDTO.setAmount(isoObject.getTransAmount());
        withdrawMoneyDTO.setBankAccount(isoObject.getAccountNumber());
        withdrawMoneyDTO.setSourceMobile(isoObject.getMobileNumber());
        return withdrawMoneyDTO;
    }
}
