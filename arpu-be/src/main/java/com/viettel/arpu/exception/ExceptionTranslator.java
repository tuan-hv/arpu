/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.exception;

import com.viettel.arpu.constant.AppConstants;
import com.viettel.arpu.model.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Apply common behavior (exception handling etc.,) to all the REST controllers.
 *
 * @author huylv2
 */
@RestControllerAdvice
@Slf4j
public class ExceptionTranslator {

    //Common error handling

    @Autowired
    @Qualifier("validatorMessageSource")
    private MessageSource validatorMessageSource;

    private ResponseEntity<Object> badRequest(Object body) {
        return ResponseEntity.ok().body(body);
    }

    private ResponseEntity<Object> notFound(Object body) {
        return ResponseEntity.ok().body(body);
    }

    /**
     * Trả ra một lỗi khi thực hiện việc khóa một khách hàng đã bị khóa
     *
     * @param ex the exceptions, {@link CustomerHasBeenLockException}
     * @return the error response in JSON format with media type
     * application/vnd.error+json
     */
    @ExceptionHandler(CustomerHasBeenLockException.class)
    public final ResponseEntity<Object> handleCustomerHasBeenLockException(CustomerHasBeenLockException ex) {
        return badRequest(ex.toErrorResponse());
    }

    /**
     * Trả ra một lỗi khi thực hiện mở khóa 1 khách hàng chưa khóa
     *
     * @param ex the exceptions, {@link CustomerHasBeenUnLockException}
     * @return the error response in JSON format with media type
     * application/vnd.error+json
     */
    @ExceptionHandler(CustomerHasBeenUnLockException.class)
    public final ResponseEntity<Object> handleCustomerHasBeenUnLockException(CustomerHasBeenUnLockException ex) {
        return badRequest(ex.toErrorResponse());
    }

    /**
     * Trả ra một exception khi không tìm thấy thông tin customer trong database
     *
     * @param ex the exceptions, {@link CustomerNotFoundException}
     * @return the error response in JSON format with media type
     * application/vnd.error+json
     */
    @ExceptionHandler(CustomerNotFoundException.class)
    public final ResponseEntity<Object> handleCustomerNotFoundException(CustomerNotFoundException ex) {
        return notFound(ex.toErrorResponse());
    }


    /**
     * Khi upload hoặc download file hợp đồng
     * nếu truyền vào file không đúng định dạng thì sẽ trả ra thông báo lỗi
     * response status HttpStatus.BAD_REQUEST (400).
     *
     * @param ex the exceptions, {@link FileInvalidException}
     * @return the error response in JSON format with media type
     * application/vnd.error+json
     */
    @ExceptionHandler(FileInvalidException.class)
    public final ResponseEntity<Object> handleFileInvalidException(FileInvalidException ex) {
        return badRequest(ex.toErrorResponse());
    }

    /**
     * Nhận vào Id khoản vay và tìm kiếm khoản vay trong database
     * Nếu không tìm thấy khoản vay đó trong database thì phải trả ra thông báo lỗi
     * response status HttpStatus.Not_Found (404).
     *
     * @param ex the exceptions, {@link LoanNotFoundException}
     * @return the error response in JSON format with media type
     * application/vnd.error+json
     */
    @ExceptionHandler(LoanNotFoundException.class)
    public final ResponseEntity<Object> handleLoanNotFoundException(LoanNotFoundException ex) {
        return notFound(ex.toErrorResponse());
    }

    /**
     * Khi từ chối khoản vay, bắt buộc phải truyền vào lý do từ chối
     * không truyền sẽ hiển thị ra thông báo lỗi
     * response status HttpStatus.BAD_REQUEST (400).
     *
     * @param ex the exceptions, {@link ReasonMissingException}
     * @return the error response in JSON format with media type
     * application/vnd.error+json
     */
    @ExceptionHandler(ReasonMissingException.class)
    public final ResponseEntity<Object> handleReasonMissingException(ReasonMissingException ex) {
        return notFound(ex.toErrorResponse());
    }

    /**
     * Thực hiện gửi duyệt đối với những khoản vay ở trạng thái đang chờ MB phê duyệt
     * khi thực hiện action với những khoản vay chưa chuyển trạng thái
     * sẽ có thông báo lỗi xảy ra
     * response status HttpStatus.BAD_REQUEST (400).
     *
     * @param ex the exceptions, {@link LoanStatusInvalidException}
     * @return the error response in JSON format with media type
     * application/vnd.error+json
     */
    @ExceptionHandler(LoanStatusInvalidException.class)
    public final ResponseEntity<Object> handleLoanStatusInvalidException(LoanStatusInvalidException ex) {
        return notFound(ex.toErrorResponse());
    }

    /**
     * Khi update trạng thái 1 khoản vay mà truyền vào 1 mã code không hợp lệ
     * response status HttpStatus.BAD_REQUEST (400).
     *
     * @param ex the exceptions, {@link CodeInvalidException}
     * @return the error response in JSON format with media type
     * application/vnd.error+json
     */
    @ExceptionHandler(CodeInvalidException.class)
    public final ResponseEntity<Object> handleCodeInvalidException(CodeInvalidException ex) {
        return notFound(ex.toErrorResponse());
    }

    /**
     * Trả ra thông báo lỗi khi truyền vào code id không tìm thấy trong database
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(CodeNotFoundException.class)
    public final ResponseEntity<Object> handleCodeINotFoundException(CodeNotFoundException ex) {
        return notFound(ex.toErrorResponse());
    }
    /**
     * Nếu thực hiện action phê duyệt hoặc từ chối với 1 khoản vay đã được phê duyệt hoặc từ chối
     * Thì sẽ trả ra thông báo lỗi
     * response status HttpStatus.BAD_REQUEST (400).
     *
     * @param ex the exceptions, {@link LoanHasBeenApprovedException}
     * @return the error response in JSON format with media type
     * application/vnd.error+json
     */
    @ExceptionHandler(LoanHasBeenApprovedException.class)
    public final ResponseEntity<Object> loanHasBeenApprovedFoundException(LoanHasBeenApprovedException ex) {
        return notFound(ex.toErrorResponse());
    }

    /**
     * Check data khi call API với data trong databale
     * Nếu không khớp nhau thì sẽ thông báo lỗi
     * response status HttpStatus.BAD_REQUEST (400).
     *
     * @param ex the exceptions, {@link DataInvalidException}
     * @return the error response in JSON format with media type
     * application/vnd.error+json
     */
    @ExceptionHandler(DataInvalidException.class)
    public final ResponseEntity<Object> dataInvalidException(DataInvalidException ex) {
        return notFound(ex.toErrorResponse());
    }


    /**
     * Trả về 1 exception khi quá trình thực hiện socket bị timeout
     *
     * @param ex the exceptions, {@link SocketTimeoutException}, {@link ResourceAccessException}
     * @return the error response in JSON format with media type
     * application/vnd.error+json
     */
    @ExceptionHandler({SocketTimeoutException.class, ResourceAccessException.class})
    public final ResponseEntity<Object> handleSocketTimeoutException(Exception ex) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ErrorResponse(AppConstants.CONNECTION_TIMEOUT));
    }


    /**
     * Trả ra lỗi khi không tìm thấy MB
     *
     * @param ex the exceptions, {@link MbNotFoundException}
     * @return the error response in JSON format with media type
     * application/vnd.error+json
     */
    @ExceptionHandler(MbNotFoundException.class)
    public final ResponseEntity<Object> handleMbNotFoundException(MbNotFoundException ex) {
        return notFound(new ErrorResponse(AppConstants.BAD_REQUEST, Collections.singletonList(ex.getMessage())));
    }

    /**
     * Trả ra một thông báo lỗi khi kết quả trả về từ MB bị lỗi
     *
     * @param ex the exceptions, {@link MbResponseException}
     * @return the error response in JSON format with media type
     * application/vnd.error+json
     */
    @ExceptionHandler(MbResponseException.class)
    public final ResponseEntity<Object> handleMbResponseException(MbResponseException ex) {
        return notFound(new ErrorResponse(AppConstants.VALIDATE_MB_FAIL, Collections.singletonList(ex.getMessage())));
    }

    /**
     * Trả ra khi otp sai
     *
     * @param ex the exceptions, {@link MbOtpFailException}
     * @return the error response in JSON format with media type
     * application/vnd.error+json
     */
    @ExceptionHandler(MbOtpFailException.class)
    public final ResponseEntity<Object> handleMbOtpFailException(MbOtpFailException ex) {
        return badRequest(ex.toErrorResponse());
    }

    /**
     * The exception handler is trigger if a JSR303 {@link BindException}
     * is being raised.
     * <p>
     * Log the exception message at warn level and stack trace as trace level. Return
     * response status HttpStatus.BAD_REQUEST (400).
     *
     * @param e the exceptions, {@link BindException}
     * @return the error response in JSON format with media type
     * application/vnd.error+json
     */
    @ExceptionHandler({BindException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ErrorResponse onBindException(BindException e) {
        return getErrorResponse(e.getBindingResult());
    }

    /**
     * The exception handler is trigger if a JSR303 {@link MethodArgumentNotValidException}
     * is being raised.
     * <p>
     * Log the exception message at warn level and stack trace as trace level. Return
     * response status HttpStatus.BAD_REQUEST (400).
     *
     * @param e the exceptions, {@link MethodArgumentNotValidException}
     * @return the error response in JSON format with media type
     * application/vnd.error+json
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ErrorResponse onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return getErrorResponse(e.getBindingResult());
    }

    private ErrorResponse getErrorResponse(BindingResult bindingResult) {
        ErrorResponse errorResponse = new ErrorResponse(AppConstants.BAD_REQUEST);

        for (ObjectError error : bindingResult.getAllErrors()) {
            Optional.ofNullable(error.getCodes()).ifPresent((String[] codes) -> {
                if (codes.length > 0) {
                    String errorCode = codes[0];
                    errorResponse.getDetails().add(validatorMessageSource
                            .getMessage(errorCode, null, LocaleContextHolder.getLocale()));
                }
            });
        }

        return errorResponse;
    }

    /**
     * Handle the case where no handler was found during the dispatch.
     * <p>The default implementation sends an HTTP 404 error and returns an empty
     *
     * @param ex the NoHandlerFoundException to be handled
     *           at the time of the exception (for example, if multipart resolution failed)
     * @return an ErrorResponse object indicating the exception was handled
     * @since 4.0
     */
    @ResponseStatus(code = HttpStatus.OK)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ErrorResponse onNotFoundException(NoHandlerFoundException ex) {
        return new ErrorResponse(AppConstants.NOT_FOUND);
    }

    /**
     * Handles the general error case. Log track trace at error level
     *
     * @param e the exception not handled by other exception handler methods
     * @return the error response in JSON format with media type
     * application/vnd.error+json
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ErrorResponse onException(Exception e) {
        log.error("Caught exception while handling a request", e);
        String msg = getExceptionMessage(e);
        List<String> details = new ArrayList<>();
        details.add(msg);
        return new ErrorResponse(AppConstants.INTERNAL_SERVER_ERROR.getCode(),
                AppConstants.INTERNAL_SERVER_ERROR.getMessage(),
                details);
    }


    /**
     * Client did not formulate a correct request. Log the exception message at warn level
     * and stack trace as trace level. Return response status HttpStatus.BAD_REQUEST
     * (400).
     * {@link UnsatisfiedServletRequestParameterException},
     * {@link HttpMessageNotReadableException}, or
     * {@link HttpRequestMethodNotSupportedException}, or
     * {@link MissingServletRequestParameterException}, or
     *
     * @return the error response in JSON format with media type
     * application/vnd.error+json
     */
    @ExceptionHandler({
            MissingServletRequestParameterException.class,
            HttpMessageNotReadableException.class,
            HttpRequestMethodNotSupportedException.class,
            UnsatisfiedServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ErrorResponse onClientGenericBadRequest(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse(AppConstants.BAD_REQUEST);
        return errorResponse;
    }

    /**
     * Get stacktrace from the exception
     * @param e the exception
     * @return the stacktrace
     */
    private String getExceptionMessage(Exception e) {
        return ExceptionUtils.getStackTrace(e);
    }

    /**
     * Trả ra exception khi rút tiền từ MB bị lỗi
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(WithdrawMoneyException.class)
    public final ResponseEntity<Object> handleWithdrawMoneyException(WithdrawMoneyException ex) {
        return badRequest(ex.toErrorResponse());
    }
}
