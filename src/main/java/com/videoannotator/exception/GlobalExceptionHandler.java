package com.videoannotator.exception;

import com.videoannotator.constant.ResponseCodeEnum;
import com.videoannotator.model.response.ErrorResponse;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler {
    private final MessageSource messageSource;

    @ExceptionHandler({NotFoundException.class, EntityNotFoundException.class})
    public ResponseEntity<ErrorResponse> resourceNotFound() {
        var response = new ErrorResponse();
        response.setCode(ResponseCodeEnum.NOK.getValue());
        response.setMessage(messageSource.getMessage("error.not.found", null, LocaleContextHolder.getLocale()));
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({EmailAlreadyExistException.class})
    public ResponseEntity<ErrorResponse> emailExisted() {
        var response = new ErrorResponse();
        response.setCode(ResponseCodeEnum.NOK.getValue());
        response.setMessage(messageSource.getMessage("error.email.existed", null, LocaleContextHolder.getLocale()));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> invalidInput(MethodArgumentNotValidException e) {
        var response = new ErrorResponse();
        List<String> errorList = e.getBindingResult().getFieldErrors().stream().map(error -> error.getField() + " " + error.getDefaultMessage()).collect(Collectors.toList());
        response.setCode(ResponseCodeEnum.NOK.getValue());
        if (errorList.isEmpty()) {
            response.setMessage(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        } else {
            response.setMessage(messageSource.getMessage("error.invalid.input", null, LocaleContextHolder.getLocale()));
            response.setError(errorList);
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<ErrorResponse> usernamePassword() {
        var response = new ErrorResponse();
        response.setCode(ResponseCodeEnum.NOK.getValue());
        response.setMessage(messageSource.getMessage("error.invalid.login", null, LocaleContextHolder.getLocale()));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NotLoginException.class})
    public ResponseEntity<ErrorResponse> userNotLogin() {
        var response = new ErrorResponse();
        response.setCode(ResponseCodeEnum.NOK.getValue());
        response.setMessage(messageSource.getMessage("error.user.not.login", null, LocaleContextHolder.getLocale()));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({PermissionDeniedException.class})
    public ResponseEntity<ErrorResponse> permissionDenied() {
        var response = new ErrorResponse();
        response.setCode(ResponseCodeEnum.NOK.getValue());
        response.setMessage(messageSource.getMessage("error.permission.denied", null, LocaleContextHolder.getLocale()));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
