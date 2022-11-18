package com.hcmute.tdshop.exception;

import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.utils.Helper;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@ResponseStatus
@Slf4j
public class RestResponseEntityExceptionHandler {

  @ExceptionHandler(RuntimeException.class)
  public DataResponse generalException(RuntimeException exception) {
    writeLogs(exception);
    return new DataResponse(ApplicationConstants.BAD_REQUEST, exception.getMessage(), null,
        HttpStatus.BAD_REQUEST.value());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public DataResponse failValidRequestBody(MethodArgumentNotValidException exception, WebRequest request) {
    writeLogs(exception);
    String errors = Helper.generateMethodArgumentNotValidExceptionMessage(exception);
    return new DataResponse(ApplicationConstants.BAD_REQUEST, errors, null, HttpStatus.BAD_REQUEST.value());
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public DataResponse handleHttpMessageNotReadable(HttpMessageNotReadableException exception, WebRequest request) {
    writeLogs(exception);
    return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.UNEXPECTED_ERROR, null,
        HttpStatus.BAD_REQUEST.value());
  }

  private void writeLogs(Exception exception) {
    log.error(exception.getMessage());
  }
}
