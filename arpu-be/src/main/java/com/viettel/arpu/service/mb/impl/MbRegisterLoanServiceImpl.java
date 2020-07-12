/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.service.mb.impl;

import com.viettel.arpu.config.MbStorageProperties;
import com.viettel.arpu.constant.AppConstants;
import com.viettel.arpu.constant.CommonConstant;
import com.viettel.arpu.constant.HeaderConstants;
import com.viettel.arpu.constant.MBConstant;
import com.viettel.arpu.constant.ParamsHeader;
import com.viettel.arpu.constant.enums.ApprovalStatus;
import com.viettel.arpu.constant.enums.LoanStatus;
import com.viettel.arpu.constant.enums.Relationship;
import com.viettel.arpu.exception.MbNotFoundException;
import com.viettel.arpu.locale.Translator;
import com.viettel.arpu.model.dto.mb.MbLoanLimitDTO;
import com.viettel.arpu.model.entity.*;
import com.viettel.arpu.model.request.mb.MbChangeLimitForm;
import com.viettel.arpu.model.request.mb.MbConfirmCreateLoanForm;
import com.viettel.arpu.model.request.mb.MbConfirmFinalLoanInMbForm;
import com.viettel.arpu.model.request.mb.MbConfirmLimitForm;
import com.viettel.arpu.model.request.mb.MbConfirmPayInMbForm;
import com.viettel.arpu.model.request.mb.MbDetailLoanForm;
import com.viettel.arpu.model.request.mb.MbFinalLoanForm;
import com.viettel.arpu.model.request.mb.MbGetLimitForm;
import com.viettel.arpu.model.request.mb.MbGetLimitInMBForm;
import com.viettel.arpu.model.request.mb.MbHistoriesLoanForm;
import com.viettel.arpu.model.request.mb.MbIncreaseLimitForm;
import com.viettel.arpu.model.request.mb.MbLoanRegistrationForm;
import com.viettel.arpu.model.request.mb.MbOtpForm;
import com.viettel.arpu.model.request.mb.MbPayLoanForm;
import com.viettel.arpu.model.request.mb.MbPinForm;
import com.viettel.arpu.model.request.mb.MbReduceLimitForm;
import com.viettel.arpu.model.request.mb.MbSendToMbApprovalForm;
import com.viettel.arpu.model.request.mb.MbVerifyOtpForm;
import com.viettel.arpu.model.request.mb.PayLoadForm;
import com.viettel.arpu.model.request.mb.VtConfirmForm;
import com.viettel.arpu.model.request.mb.VtValidatePinForm;
import com.viettel.arpu.model.request.mb.VtvalidateOtpForm;
import com.viettel.arpu.model.response.mb.MbBaseResponse;
import com.viettel.arpu.model.response.mb.MbConfirmCreateLoanResponse;
import com.viettel.arpu.model.response.mb.MbDetailLoanResponse;
import com.viettel.arpu.model.response.mb.MbHistoriesLoanResponse;
import com.viettel.arpu.model.response.mb.VtOtpConfirmResponse;
import com.viettel.arpu.model.response.mb.VtOtpResponse;
import com.viettel.arpu.model.response.mb.VtOtpResultResponse;
import com.viettel.arpu.model.response.mb.VtPinResponse;
import com.viettel.arpu.model.response.mb.VtStatusResponse;
import com.viettel.arpu.repository.CodeCodeRepository;
import com.viettel.arpu.repository.CustomerRepository;
import com.viettel.arpu.repository.InterestRepository;
import com.viettel.arpu.repository.LoanRepository;
import com.viettel.arpu.repository.ReferenceRepository;
import com.viettel.arpu.service.mb.AbstractMbService;
import com.viettel.arpu.service.mb.MbRegisterLoanService;
import com.viettel.arpu.utils.DateUtils;
import com.viettel.arpu.utils.GenerateRequestIdUtils;
import com.viettel.arpu.utils.ObjectMapperUtils;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @Author VuHQ
 * @Since 5/29/2020
 */
@Service
@Setter
public class MbRegisterLoanServiceImpl extends AbstractMbService implements MbRegisterLoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private MbStorageProperties mbStorageProperties;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CodeCodeRepository codeCodeRepository;

    @Autowired
    private InterestRepository interestRepository;

    @Autowired
    private ReferenceRepository referenceRepository;

    private String identifierDateDefault = MBConstant.DATE_IDENTIFIER;

    /**
     * @description gửi thông tin khoản vay cho MB để kiểm duyệt
     * @param mbLoanRegistrationForm
     * @return
     */
    @Override
    public MbBaseResponse sendToMBApprove(@Valid MbLoanRegistrationForm mbLoanRegistrationForm) {
        Customer customer = getCustomerByMsisdn(mbLoanRegistrationForm.getSourceMobile());
        MbSendToMbApprovalForm mbSendToMbApprovalForm = ObjectMapperUtils
                .map(mbLoanRegistrationForm, MbSendToMbApprovalForm.class);
        if (!StringUtils.isEmpty(mbLoanRegistrationForm.getReferencerType())) {
            mbSendToMbApprovalForm.setReferencerType(Relationship.
                    valueOf(mbLoanRegistrationForm.getReferencerType()).getId());
        }

        mbSendToMbApprovalForm.setPayType(mbLoanRegistrationForm.getPayType().getId());
        mbSendToMbApprovalForm.setScoreAve(customer.getArpuLatestThreeMonths());
        mbSendToMbApprovalForm.setScoreMax(customer.getScoreMax());
        mbSendToMbApprovalForm.setScoreMin(customer.getScoreMin());
        mbSendToMbApprovalForm.setEmail(Optional.ofNullable(mbLoanRegistrationForm.getEmail())
                .orElse(MBConstant.NO_EMAIL));

        MbBaseResponse mbBaseResponse = sendToMbAPI(mbSendToMbApprovalForm, HttpMethod.POST
                , mbStorageProperties.getCreateloan(), MbBaseResponse.class
                , HeaderConstants.POST_CREATE_LOAN, mbLoanRegistrationForm.getRequestId());

        verifyAndSaveRequestToStorage(mbLoanRegistrationForm, mbBaseResponse);
        return mbBaseResponse;
    }

    /**
     * @description gửi xác nhận tạo khoản vay cho MB đồng thời lưu thông tin khoản vay xuống DB
     * @param mbConfirmCreateLoanForm
     * @return
     */
    @Override
    @Transactional
    public MbBaseResponse confirmCreateLoan(@Valid MbConfirmCreateLoanForm mbConfirmCreateLoanForm) {
        return convertFormToEntity(mbConfirmCreateLoanForm);
    }

    /**
     * @description gửi yêu cầu lấy sao kê giao dịch
     * @param mbHistoriesLoanForm
     * @return
     */
    @Override
    public MbHistoriesLoanResponse getHistories(@Valid MbHistoriesLoanForm mbHistoriesLoanForm) {
        return sendToMbAPI(mbHistoriesLoanForm, HttpMethod.POST
                , mbStorageProperties.getHistoryloan(), MbHistoriesLoanResponse.class
                , HeaderConstants.POST_HISTORY_LOAN, mbHistoriesLoanForm.getRequestId());
    }

    /**
     * @description gửi thông tin thay đổi hạn mức cho MB
     * @param id
     * @param mbChangeLimitForm
     * @return
     */
    @Override
    public MbBaseResponse checkLoanLimit(@Valid MbChangeLimitForm mbChangeLimitForm, Long id) {
        MbBaseResponse mbBaseResponse;
        Loan loanResult = getLoanById(id);
        String requestId = mbChangeLimitForm.getRequestId();
        if ((mbChangeLimitForm.getChangeAmount()).compareTo(loanResult.getLoanAmount()) <= 0) {
            mbChangeLimitForm.setIncrease(false);
            mbBaseResponse = sendToCheckReduceLimitApi(mbChangeLimitForm, mbStorageProperties.getReducelimit(), requestId);
        } else {
            mbChangeLimitForm.setIncrease(true);
            mbBaseResponse = sendToCheckIncreaseLimitApi(mbChangeLimitForm, mbStorageProperties.getIncreaselimit(), requestId);
        }

        verifyAndSaveRequestToStorage(mbChangeLimitForm, mbBaseResponse);
        return mbBaseResponse;
    }

    /**
     * @description lấy thông tin khoản vay theo id
     * @param id
     * @return
     */
    private Loan getLoanById(Long id) {
        return loanRepository.findById(id).orElseThrow(() -> new MbNotFoundException("error.msg.loan.notfound"));
    }

    /**
     * @description gửi thông tin tăng hạn mức cho MB kiểm tra
     * @param mbChangeLimitForm
     * @param url
     * @return
     */
    private MbBaseResponse sendToCheckIncreaseLimitApi(MbChangeLimitForm mbChangeLimitForm, String url, String requestId) {
        MbIncreaseLimitForm mbIncreaseLimitForm = ObjectMapperUtils.map(mbChangeLimitForm, MbIncreaseLimitForm.class);
        mbIncreaseLimitForm.setIncreaseAmount(String.valueOf(mbChangeLimitForm.getChangeAmount().intValue()));

        return sendToMbAPI(mbIncreaseLimitForm, HttpMethod.POST, url, MbBaseResponse.class
                , HeaderConstants.POST_INCREASE_LIMIT, requestId);
    }

    /**
     * @description gửi thông tin giảm hạn mức cho MB kiểm tra
     * @param mbChangeLimitForm
     * @param url
     * @return
     */
    private MbBaseResponse sendToCheckReduceLimitApi(MbChangeLimitForm mbChangeLimitForm, String url, String requestId) {
        MbReduceLimitForm formSendToMb = ObjectMapperUtils.map(mbChangeLimitForm, MbReduceLimitForm.class);
        formSendToMb.setReduceAmount(String.valueOf(mbChangeLimitForm.getChangeAmount().intValue()));

        return sendToMbAPI(formSendToMb, HttpMethod.POST, url, MbBaseResponse.class
                , HeaderConstants.POST_REDUCE_LIMIT, requestId);
    }

    /**
     * @description gửi xác nhận thay đổi hạn mức cho MB
     * @param mbConfirmLimitForm
     * @return
     */
    @Override
    @Transactional
    public MbBaseResponse confirmLimit(@Valid MbConfirmLimitForm mbConfirmLimitForm) {
        MbBaseResponse mbBaseResponse;
        String requestId = getRequestId();
        MbChangeLimitForm mbChangeLimitForm = (MbChangeLimitForm) getObjectFormInStorage(requestId);
        if (mbChangeLimitForm.isIncrease()) {
            mbBaseResponse = sendToConfirmIncreaseLimitApi(mbConfirmLimitForm.getOtp()
                    , mbChangeLimitForm, mbStorageProperties.getIncreaselimit(), mbConfirmLimitForm.getRequestId());
        } else {
            mbBaseResponse = sendToConfirmReduceLimitApi(mbConfirmLimitForm.getOtp()
                    , mbChangeLimitForm, mbStorageProperties.getReducelimit(), mbConfirmLimitForm.getRequestId());
        }

        removeRequestIdInStorage(requestId);
        if (!AppConstants.OK.getCode().equalsIgnoreCase(mbBaseResponse.getErrorCode())) {
            return mbBaseResponse;
        }

        Loan loanResult = getLoanById(mbChangeLimitForm.getId());
        loanResult.setLoanAmount(mbChangeLimitForm.getChangeAmount());
        loanRepository.save(loanResult);
        return mbBaseResponse;
    }

    /**
     * @description gửi xác nhận giảm hạn mức cho MB
     * @param otp
     * @param mbChangeLimitForm
     * @param url
     * @return
     */
    private MbBaseResponse sendToConfirmReduceLimitApi(String otp, MbChangeLimitForm mbChangeLimitForm, String url, String requestId) {
        MbReduceLimitForm mbReduceLimitForm = ObjectMapperUtils.map(mbChangeLimitForm, MbReduceLimitForm.class);
        mbReduceLimitForm.setReduceRequestId(mbChangeLimitForm.getRequestId());
        mbReduceLimitForm.setOtp(otp);

        return sendToMbAPI(mbReduceLimitForm, HttpMethod.PUT, url, MbBaseResponse.class
                , HeaderConstants.PUT_REDUCE_LIMIT, requestId);
    }

    /**
     * @description gửi xác nhận tăng hạn mức cho MB
     * @param otp
     * @param mbChangeLimitForm
     * @param url
     * @return
     */
    private MbBaseResponse sendToConfirmIncreaseLimitApi(String otp, MbChangeLimitForm mbChangeLimitForm, String url, String requestId) {
        MbIncreaseLimitForm mbIncreaseLimitForm = ObjectMapperUtils.map(mbChangeLimitForm, MbIncreaseLimitForm.class);
        String requestLimitId = mbChangeLimitForm.getRequestId();
        mbIncreaseLimitForm.setIncreaseRequestId(requestLimitId);
        mbIncreaseLimitForm.setOtp(otp);

        return sendToMbAPI(mbIncreaseLimitForm, HttpMethod.PUT, url, MbBaseResponse.class
                , HeaderConstants.PUT_INCREASE_LIMIT, requestId);
    }

    /**
     * @description gửi thông tin trả nợ 1 phần sang cho MB kiểm tra
     * @param mbPayLoanForm
     * @return
     */
    @Override
    public MbBaseResponse checkPayLoan(@Valid MbPayLoanForm mbPayLoanForm) {
        String requestId = mbPayLoanForm.getRequestId();
        MbBaseResponse mbBaseResponse = sendToMbAPI(mbPayLoanForm, HttpMethod.POST, mbStorageProperties.getPayloan()
                , MbBaseResponse.class, HeaderConstants.POST_PAY_LOAN, requestId);

        mbPayLoanForm.setRequestId(requestId);
        verifyAndSaveRequestToStorage(mbPayLoanForm, mbBaseResponse);
        return mbBaseResponse;
    }

    /**
     * @description gửi otp và các thông tin liên quan sang cho MB confirm trả nợ 1 phần.
     * @param mbConfirmLimitForm
     * @return
     */
    @Override
    public MbBaseResponse confirmPayLoan(@Valid MbConfirmLimitForm mbConfirmLimitForm) {
        MbPayLoanForm mbPayLoanForm = (MbPayLoanForm) getObjectFormInStorage(getRequestId());
        String requestId = getRequestId();
        MbConfirmPayInMbForm mbConfirmPayInMbForm = ObjectMapperUtils.map(mbPayLoanForm, MbConfirmPayInMbForm.class);
        mbConfirmPayInMbForm.setOtp(mbConfirmLimitForm.getOtp());
        mbConfirmPayInMbForm.setPayRequestId(mbPayLoanForm.getRequestId());

        MbBaseResponse mbBaseResponse = sendToMbAPI(mbConfirmPayInMbForm, HttpMethod.PUT, mbStorageProperties.getPayloan()
                , MbBaseResponse.class, HeaderConstants.PUT_PAY_LOAN, mbConfirmLimitForm.getRequestId());

        removeRequestIdInStorage(requestId);
        return mbBaseResponse;
    }

    /**
     * @description gửi thông tin tất toán sang cho MB kiểm tra
     * @param mbFinalLoanForm
     * @return
     */
    @Override
    public MbBaseResponse checkFinalLoan(@Valid MbFinalLoanForm mbFinalLoanForm) {
        String requestId = mbFinalLoanForm.getRequestId();
        MbBaseResponse mbBaseResponse = sendToMbAPI(mbFinalLoanForm, HttpMethod.POST, mbStorageProperties.getFinalloan()
                , MbBaseResponse.class, HeaderConstants.POST_FINAL_LOAN, requestId);

        mbFinalLoanForm.setRequestId(requestId);
        verifyAndSaveRequestToStorage(mbFinalLoanForm, mbBaseResponse);
        return mbBaseResponse;
    }

    /**
     * @description gửi xác nhận tất toán sang cho MB
     * @param confirmLimitForm
     * @return
     */
    @Override
    public MbBaseResponse confirmFinalLoan(@Valid MbConfirmLimitForm confirmLimitForm) {
        String requestId = getRequestId();
        MbFinalLoanForm mbFinalLoanForm = (MbFinalLoanForm) getObjectFormInStorage(requestId);
        MbConfirmFinalLoanInMbForm mbConfirmFinalLoanInMbForm = ObjectMapperUtils.map(mbFinalLoanForm
                , MbConfirmFinalLoanInMbForm.class);
        mbConfirmFinalLoanInMbForm.setFinalRequestId(mbFinalLoanForm.getRequestId());
        mbConfirmFinalLoanInMbForm.setOtp(confirmLimitForm.getOtp());
        MbBaseResponse mbBaseResponse = sendToMbAPI(mbConfirmFinalLoanInMbForm, HttpMethod.PUT, mbStorageProperties.getFinalloan()
                , MbBaseResponse.class, HeaderConstants.PUT_FINAL_LOAN, confirmLimitForm.getRequestId());

        removeRequestIdInStorage(requestId);

        if (!AppConstants.OK.getCode().equalsIgnoreCase(mbBaseResponse.getErrorCode())) {
            return mbBaseResponse;
        }

        Loan loanResult = getLoanById(mbFinalLoanForm.getId());
        codeCodeRepository.findById(LoanStatus.COMPLETED.getStatus())
                .ifPresent(loanResult::setLoanStatus);
        loanRepository.save(loanResult);
        return mbBaseResponse;
    }

    /**
     * @description lấy thông tin hạn mức từ bên MB
     * @param mbGetLimitForm
     * @return
     */
    @Override
    public MbLoanLimitDTO getLoanLimitInMB(@Valid MbGetLimitForm mbGetLimitForm) {
        Customer customer = getCustomerByMsisdn(mbGetLimitForm.getSourceMobile());
        MbGetLimitInMBForm mbGetLimitInMBForm = ObjectMapperUtils.map(mbGetLimitForm, MbGetLimitInMBForm.class);
        mbGetLimitInMBForm.setIdentityCardNumber(customer.getIdentityNumber());
        mbGetLimitInMBForm.setIdentityCardType(customer.getIdentityType());

        MbLoanLimitDTO mbLoanLimitDTO = sendToMbAPI(mbGetLimitInMBForm, HttpMethod.POST
                , mbStorageProperties.getCheckloan()
                , MbLoanLimitDTO.class, HeaderConstants.POST_CHECK_LOAN, mbGetLimitForm.getRequestId());

        if (!AppConstants.OK.getCode().equalsIgnoreCase(mbLoanLimitDTO.getErrorCode())
                || StringUtils.isEmpty(mbGetLimitForm.getLoanAccount())) {
            return mbLoanLimitDTO;
        }

        MbDetailLoanForm mbDetailLoanForm = ObjectMapperUtils.map(mbGetLimitForm, MbDetailLoanForm.class);
        MbDetailLoanResponse mbDetailLoanResponse = sendToMbAPI(mbDetailLoanForm,
                HttpMethod.GET,createUrlForGetLoanAmount(mbGetLimitForm),MbDetailLoanResponse.class,
                HeaderConstants.GET_DETAIL_LOAN, GenerateRequestIdUtils.generateRequestId());

        ObjectMapperUtils.map(mbDetailLoanResponse, mbLoanLimitDTO);
        mbLoanLimitDTO.setLoanAmount(Optional.ofNullable(mbDetailLoanResponse.getLoanAmount())
                .orElse(CommonConstant.DEFAULT_LIMIT));

        return mbLoanLimitDTO;
    }

    private String createUrlForGetLoanAmount(MbGetLimitForm mbGetLimitForm) {
        StringBuilder url = new StringBuilder(mbStorageProperties.getDetailloan());
        url.append("?sourceMobile=");
        url.append(mbGetLimitForm.getSourceMobile());
        url.append("&loanAccount=");
        url.append(mbGetLimitForm.getLoanAccount());
        return url.toString();
    }

    /**
     * @description lưu thông tin khoản vay xuống db
     * @param mbConfirmCreateLoanForm
     * @return
     */
    @Transactional
    public MbBaseResponse convertFormToEntity(@Valid MbConfirmCreateLoanForm mbConfirmCreateLoanForm) {
        String requestId = getRequestId();
        MbLoanRegistrationForm mbLoanRegistrationForm = (MbLoanRegistrationForm) getObjectFormInStorage(requestId);

        ObjectMapperUtils.map(mbLoanRegistrationForm, mbConfirmCreateLoanForm);
        mbConfirmCreateLoanForm.setLoanRequestId(mbLoanRegistrationForm.getRequestId());

        MbConfirmCreateLoanResponse mbBaseResponse = sendToMbConfirm(mbConfirmCreateLoanForm);

        if (!AppConstants.OK.getCode().equalsIgnoreCase(mbBaseResponse.getErrorCode())) {
            removeRequestIdInStorage(requestId);
            return mbBaseResponse;
        }
        Loan loan = updateLoanWithResponseInMb(mbBaseResponse);

        Customer customer = getCustomerByMsisdn(mbLoanRegistrationForm.getSourceMobile());
        customer.setCustomerAccount(mbBaseResponse.getCustomerAccount());

        if (!StringUtils.isEmpty(mbLoanRegistrationForm.getReferencerType())) {
            Set<CustomerRef> customerRefs = Optional.ofNullable(customer.getCustomerRef()).orElseGet(HashSet::new);

            CustomerRef customerRef = getCustomerRef(mbLoanRegistrationForm, customer
                    , getReference(mbLoanRegistrationForm));
            customerRefs.add(customerRef);
            customer.setCustomerRef(customerRefs);

            loan.setCustomerRef(customerRef);
        }

        customer.setAddress(getAddress(mbLoanRegistrationForm, customer.getAddress()));

        loan.setCustomer(customer);
        loan.setLoanAmount(mbLoanRegistrationForm.getAmount());
        loan.setFee(mbLoanRegistrationForm.getFee());

        loan.setInterest(getInterest(mbBaseResponse.getTerm(), mbBaseResponse.getInterestRate()));
        loan.setMaximumLimit(customer.getLoanMaximum());
        loan.setMinimumLimit(customer.getLoanMinimum());
        loan.setArpuLatestThreeMonths(customer.getArpuLatestThreeMonths());
        loan.setRepaymentForm(Translator
                .toLocale(mbLoanRegistrationForm.getPayType().name()));

        codeCodeRepository.findById(LoanStatus.NOT_BORROWED.getStatus()).ifPresent(loan::setLoanStatus);
        loan.setApprovalStatus(getStatusByIdentifier());
        loan.setIsAutomaticPayment(mbLoanRegistrationForm.getIsAutomaticPayment());
        loanRepository.save(loan);
        removeRequestIdInStorage(requestId);

        mbBaseResponse.setIsBefore(isBeforeIdentifierDate());
        return mbBaseResponse;
    }

    /**
     * @description thêm các thông tin được MB trả về vào db
     * @param mbBaseResponse
     * @return
     */
    private Loan updateLoanWithResponseInMb(MbConfirmCreateLoanResponse mbBaseResponse) {
        Loan loan = new Loan();
        loan.setLoanAccount(mbBaseResponse.getLoanAccount());
        loan.setCreatedDate(mbBaseResponse.getStartDate());
        loan.setExpirationDate(mbBaseResponse.getEndDate().toLocalDate());
        return loan;
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
     * @description gửi xác nhận tạo tài khoản cho MB
     * @param mbConfirmCreateLoanForm
     * @return
     */
    private MbConfirmCreateLoanResponse sendToMbConfirm(MbConfirmCreateLoanForm mbConfirmCreateLoanForm) {
        return createLoanSendToMbAPI(mbConfirmCreateLoanForm, HttpMethod.PUT, mbStorageProperties.getCreateloan()
                , MbConfirmCreateLoanResponse.class, HeaderConstants.PUT_CREATE_LOAN, mbConfirmCreateLoanForm.getRequestId());
    }

    /**
     * @description lấy thông tin được lưu lại trong store bằng requestId
     * @param requestId
     * @return
     */
    public Object getObjectFormInStorage(String requestId) {
        Map<String, Object> requestStorage = MbLoanRegistrationForm.requestStore;
        if (!requestStorage.containsKey(requestId)) {
            throw new MbNotFoundException("error.msg.request.storage.not.found");
        }
        return requestStorage.get(requestId);
    }

    /**
     * @description nếu kết quả trả về của MB là ok thì ms lứu request vào store
     * @param mbFinalLoanForm
     * @param mbBaseResponse
     */
    private void verifyAndSaveRequestToStorage(Object mbFinalLoanForm, MbBaseResponse mbBaseResponse) {
        if (AppConstants.OK.getCode().equalsIgnoreCase(mbBaseResponse.getErrorCode())) {
            saveRequestToStorage(getRequestId(), mbFinalLoanForm);
        }
    }

    /**
     * @description lưu yêu cầu vào storage
     * @param requestId
     * @param mbLoanRegistrationForm
     */
    private void saveRequestToStorage(String requestId, Object mbLoanRegistrationForm) {
        MbLoanRegistrationForm.requestStore.put(requestId,mbLoanRegistrationForm);
    }

    /**
     * @description xóa yêu cầu khỏi storage
     * @param requestId
     */
    private void removeRequestIdInStorage(String requestId) {
        Map<String, Object> requestStorage = MbLoanRegistrationForm.requestStore;
        if (requestStorage.containsKey(requestId)) {
            requestStorage.remove(requestId);
        }
    }

    /**
     * @description lấy thông tin lãi suất
     * @param term
     * @param interest
     * @return
     */
    private Interest getInterest(String term, String interest) {
        return Interest.builder().interestRate(interest).loanTerm(term).build();
    }

    /**
     * @description lấy requestId trên header
     * @return
     */
    public String getRequestId() {
        return Optional.ofNullable(((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes())
                .getRequest().getHeader("requestId"))
                .orElseThrow(() -> new MbNotFoundException("error.msg.request.id.not.found"));
    }

    /**
     * @description lấy thông tin địa chỉ
     * @param mbLoanRegistrationForm
     * @param addressOfCustomer
     * @return
     */
    private Address getAddress(MbLoanRegistrationForm mbLoanRegistrationForm, Address addressOfCustomer) {
        Address address = Optional.ofNullable(addressOfCustomer).orElseGet(Address::new);
        address.setVillage(mbLoanRegistrationForm.getVillage());
        address.setDistrict(mbLoanRegistrationForm.getDistrict());
        address.setProvince(mbLoanRegistrationForm.getProvince());
        address.setAddressDetail(mbLoanRegistrationForm.getAddressDetail());
        return address;
    }

    /**
     * @description lấy thông tin tham chiếu của khách hàng
     * @param mbLoanRegistrationForm
     * @param customer
     * @param reference
     * @return
     */
    private CustomerRef getCustomerRef(MbLoanRegistrationForm mbLoanRegistrationForm, Customer customer, Reference reference) {
        CustomerRef customerRef = new CustomerRef();
        codeCodeRepository.findById(mbLoanRegistrationForm.getReferencerType())
                    .ifPresent(customerRef::setRelationship);

        customerRef.setReference(reference);
        customerRef.setCustomer(customer);
        return customerRef;
    }

    /**
     * @description lấy thông tin người tham chiếu
     * @param mbLoanRegistrationForm
     * @return
     */
    private Reference getReference(MbLoanRegistrationForm mbLoanRegistrationForm) {
        Reference reference = referenceRepository.findByMsisdn(mbLoanRegistrationForm.getReferencerMobile())
                .orElseGet(Reference::new);
        reference.setFullName(mbLoanRegistrationForm.getReferencerName());
        reference.setEmail(mbLoanRegistrationForm.getReferencerEmail());
        reference.setMsisdn(mbLoanRegistrationForm.getReferencerMobile());
        return reference;
    }

    /**
     * @description lấy trạng thái theo ngày định danh
     * @return
     */
    private CodeCode getStatusByIdentifier() {
        String status = ApprovalStatus.WAIT_MB_APPROVAL.getStatus();
        if (isBeforeIdentifierDate()) {
            status = ApprovalStatus.WAIT_FINALIZING_CASE.getStatus();
        }

        return codeCodeRepository.findById(status)
                .orElseThrow(() -> new MbNotFoundException("error.msg.status.not.found"));
    }

    /**
     * @description kiểm tra có phải trước ngày định danh không
     * @return
     */
    private boolean isBeforeIdentifierDate() {
        Date identifierDate = DateUtils.convertStringToDate(identifierDateDefault);
        return !org.apache.commons.lang3.time.DateUtils.isSameDay(identifierDate, new Date()) && (new Date().before(identifierDate));
    }

    /**
     * @description gửi request kiểm tra mã pin sang cho viettel
     * @param mbPinForm
     * @return
     */
    @Override
    public MbBaseResponse validatePin(@Valid MbPinForm mbPinForm) {
        VtValidatePinForm vtValidatePinForm = createRequestSendToVT(mbPinForm);
        ParamsHeader paramsHeader = setParamHeaders(mbPinForm);
        String url = mbStorageProperties.getValidatePin();
        VtPinResponse vtPinResponse = sendToMbAPI(vtValidatePinForm, HttpMethod.POST, url,
                VtPinResponse.class, paramsHeader, mbPinForm.getRequestId());

        vtPinResponse.setErrorCode(vtPinResponse.getVtStatusResponse().getCode());
        vtPinResponse.setErrorDesc(vtPinResponse.getVtStatusResponse().getMessage());
        return vtPinResponse;
    }

    /**
     * @descritpion tạo header
     * @param mbPinForm
     * @return
     */
    private ParamsHeader setParamHeaders(MbPinForm mbPinForm) {
        return ParamsHeader.paramsHeader().product(mbPinForm.getProduct())
                .xRequestId(mbPinForm.getRequestId()).appToken(mbPinForm.getAppToken());
    }

    /**
     * @description tạo request gửi sang cho VT
     * @param mbPinForm
     * @return
     */
    private VtValidatePinForm createRequestSendToVT(MbPinForm mbPinForm) {
        PayLoadForm payLoadForm = ObjectMapperUtils.map(mbPinForm, PayLoadForm.class);
        payLoadForm.setTransactionId(mbPinForm.getRequestId());
        VtValidatePinForm vtValidatePinForm = ObjectMapperUtils.map(mbPinForm, VtValidatePinForm.class);
        vtValidatePinForm.setPayLoad(payLoadForm);
        vtValidatePinForm.setType(Collections.singletonList("PIN"));
        return vtValidatePinForm;
    }

    /**
     * @description gửi yêu cầu tạo otp
     * @param mbOtpForm
     * @return
     */
    @Override
    public MbBaseResponse validateOtp(@Valid MbOtpForm mbOtpForm) {
        VtvalidateOtpForm vtvalidateOtpForm = createRequestOtpSendToVT(mbOtpForm);
        ParamsHeader paramsHeader = setParamHeadersForOTP(mbOtpForm);
        String url = mbStorageProperties.getValidatePin();
        VtOtpResponse vtOtpResponse = sendToMbAPI(vtvalidateOtpForm, HttpMethod.POST, url,
                 VtOtpResponse.class, paramsHeader, mbOtpForm.getRequestId());

        VtOtpResultResponse vtOtpResultResponse = new VtOtpResultResponse();

        VtStatusResponse vtStatusResponse = Optional.ofNullable(vtOtpResponse.getVtStatusResponse())
                .orElseGet(VtStatusResponse::new);

        vtOtpResultResponse.setErrorCode(vtStatusResponse.getCode());
        vtOtpResultResponse.setErrorDesc(vtStatusResponse.getMessage());

        if (!AppConstants.OK.getCode()
                .equalsIgnoreCase(vtStatusResponse.getCode())) {
            return vtOtpResultResponse;
        }

        vtOtpResultResponse.setSignedRequest(Optional.ofNullable(vtOtpResponse.getVtOtpDetailResponse())
                .flatMap(vtOtpDetailResponse -> Optional.ofNullable(vtOtpDetailResponse.getSignedRequest()))
                .orElse(""));

        return vtOtpResultResponse;

    }

    /**
     * @description: tạo headrer cho otp
     * @param mbOtpForm
     * @return
     */
    private ParamsHeader setParamHeadersForOTP(MbOtpForm mbOtpForm) {
        return ParamsHeader.paramsHeader().product(mbOtpForm.getProduct())
                .xRequestId(mbOtpForm.getRequestId()).appToken(mbOtpForm.getAppToken());
    }

    /**
     * @description tạo request otp gửi sang cho VT
     * @param mbOtpForm
     * @return
     */
    private VtvalidateOtpForm createRequestOtpSendToVT(MbOtpForm mbOtpForm) {
        PayLoadForm payLoadForm = ObjectMapperUtils.map(mbOtpForm, PayLoadForm.class);
        payLoadForm.setTransactionId(mbOtpForm.getRequestId());

        VtvalidateOtpForm vtValidateOtpForm = ObjectMapperUtils.map(mbOtpForm, VtvalidateOtpForm.class);
        vtValidateOtpForm.setPayLoad(payLoadForm);
        vtValidateOtpForm.setType(Collections.singletonList("OTP"));

        return vtValidateOtpForm;
    }

    /**
     * @description: xác nhận OTP
     * @param mbVerifyOtpForm
     * @return
     */
    @Override
    public MbBaseResponse verifyOtp(@Valid MbVerifyOtpForm mbVerifyOtpForm) {
        ParamsHeader paramsHeader = ParamsHeader.paramsHeader().xRequestId(mbVerifyOtpForm.getRequestId());

        VtConfirmForm vtConfirmForm = new VtConfirmForm();
        vtConfirmForm.setOtp(mbVerifyOtpForm.getOtp());
        VtOtpConfirmResponse vtOtpConfirmResponse = sendToMbAPI(vtConfirmForm, HttpMethod.POST
                , mbVerifyOtpForm.getSignedRequest(),
                VtOtpConfirmResponse.class, paramsHeader, GenerateRequestIdUtils.generateRequestId());

        VtStatusResponse vtStatusResponse = Optional.ofNullable(vtOtpConfirmResponse.getVtStatusResponse())
                .orElseGet(VtStatusResponse::new);
        vtOtpConfirmResponse.setErrorCode(vtStatusResponse.getCode());
        vtOtpConfirmResponse.setErrorDesc(vtStatusResponse.getMessage());

        return  vtOtpConfirmResponse;
    }

}
