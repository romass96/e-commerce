package ua.ugolek.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ControllerExceptionHandler
{
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ValidationErrorResponse onConstraintValidationException(ConstraintViolationException e) {
        ValidationErrorResponse error = new ValidationErrorResponse();
        for ( ConstraintViolation violation : e.getConstraintViolations()) {
            error.addViolation(
                new Violation(violation.getPropertyPath().toString(), violation.getMessage()));
        }
        return error;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ValidationErrorResponse onMethodArgumentNotValidException(
        MethodArgumentNotValidException e) {
        ValidationErrorResponse error = new ValidationErrorResponse();
        for ( FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            error.addViolation(
                new Violation(fieldError.getField(), fieldError.getDefaultMessage()));
        }
        return error;
    }

    @Data
    @NoArgsConstructor
    private static class ValidationErrorResponse {
        private List<Violation> violations = new ArrayList<>();

        public void addViolation(Violation violation) {
            violations.add(violation);
        }
    }

    @Data
    @AllArgsConstructor
    private static class Violation {
        private final String fieldName;
        private final String message;
    }
}
