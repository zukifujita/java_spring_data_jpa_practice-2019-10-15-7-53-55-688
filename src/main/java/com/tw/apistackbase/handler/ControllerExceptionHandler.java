package com.tw.apistackbase.handler;

import com.tw.apistackbase.error.CustomError;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ControllerExceptionHandler {

        @ExceptionHandler(NotFoundException.class)
        @ResponseStatus(code = HttpStatus.NOT_FOUND)
        @ResponseBody
        public CustomError handleNotFoundException(NotFoundException e) {
            CustomError customError = new CustomError();
            customError.setErrorCode(HttpStatus.NOT_FOUND.value());
            customError.setErrorMessage(e.getMessage());

            return customError;
        }

        @ExceptionHandler(NullPointerException.class)
        @ResponseStatus(code = HttpStatus.BAD_REQUEST)
        @ResponseBody
        public CustomError handleNullPointerException(NullPointerException e) {
            CustomError customError = new CustomError();
            customError.setErrorCode(HttpStatus.BAD_REQUEST.value());
            customError.setErrorMessage(e.getMessage());

            return customError;
        }
}