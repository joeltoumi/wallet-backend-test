package com.playtomic.tests.wallet.api.application.rest.error;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.playtomic.tests.wallet.api.domain.paymentclient.PaymentException;
import com.playtomic.tests.wallet.api.domain.topup.PlaytomicWalletException;
import com.playtomic.tests.wallet.api.infrastructure.persistence.exception.WalletNotFoundException;
import com.playtomic.tests.wallet.api.infrastructure.stripe.exception.StripeAmountTooSmallException;
import com.playtomic.tests.wallet.dto.ErrorDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalErrorHandler {

  @ExceptionHandler(Exception.class)
  @ResponseStatus(INTERNAL_SERVER_ERROR)
  public ResponseEntity<ErrorDTO> handleGenericError(Exception exception) {
    log.error(exception.getMessage(), exception);
    var error = new ErrorDTO().message("Technical problem");

    return ResponseEntity.status(INTERNAL_SERVER_ERROR)
        .contentType(MediaType.APPLICATION_JSON)
        .body(error);
  }

  @ExceptionHandler(value = {
          MissingServletRequestParameterException.class,
          MethodArgumentTypeMismatchException.class,
          MethodArgumentNotValidException.class,
          IllegalArgumentException.class
  })
  @ResponseStatus(BAD_REQUEST)
  public ResponseEntity<ErrorDTO> handleBadRequest(Exception exception) {
    log.warn(exception.getMessage(), exception);
    var error = new ErrorDTO().message(exception.getMessage());

    return ResponseEntity.status(BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(error);
  }

  @ExceptionHandler(WalletNotFoundException.class)
  @ResponseStatus(NOT_FOUND)
  public ResponseEntity<ErrorDTO> handleWalletNotFoundException(WalletNotFoundException exception) {
    log.error(exception.getMessage(), exception);
    var error = new ErrorDTO().message(exception.getMessage());

    return ResponseEntity.status(NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(error);
  }

  @ExceptionHandler(PaymentException.class)
  @ResponseStatus(UNPROCESSABLE_ENTITY)
  public ResponseEntity<ErrorDTO> handlePaymentException(PaymentException exception) {
    log.error(exception.getMessage(), exception);
    var error = new ErrorDTO().message(exception.getMessage());

    return ResponseEntity.status(UNPROCESSABLE_ENTITY).contentType(MediaType.APPLICATION_JSON).body(error);
  }

  @ExceptionHandler(PlaytomicWalletException.class)
  @ResponseStatus(CONFLICT)
  public ResponseEntity<ErrorDTO> handlePlaytomicWalletException(PlaytomicWalletException exception) {
    log.error(exception.getMessage(), exception);
    var error = new ErrorDTO().message(exception.getMessage());

    return ResponseEntity.status(CONFLICT).contentType(MediaType.APPLICATION_JSON).body(error);
  }
}
