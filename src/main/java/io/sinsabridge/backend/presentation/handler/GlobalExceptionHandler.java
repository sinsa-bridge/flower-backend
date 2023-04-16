package io.sinsabridge.backend.presentation.handler;

import io.sinsabridge.backend.application.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleValidationException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<ObjectError> errors = bindingResult.getAllErrors();

        // 파라미터 정보와 필드 정보 로그로 출력
        logger.error("Validation Error - Parameter : {}, Error : {}", bindingResult.getTarget(), errors);

        // ErrorResponse 객체 생성
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode("VALIDATION_ERROR");
        errorResponse.setErrorMessage("Request parameter validation failed");
        errorResponse.setErrors(getErrors(errors));

        return errorResponse;
    }

    private List<String> getErrors(List<ObjectError> errors) {
        List<String> errorMessages = new ArrayList<>();
        for (ObjectError error : errors) {
            errorMessages.add(error.getDefaultMessage());
        }
        return errorMessages;
    }
}