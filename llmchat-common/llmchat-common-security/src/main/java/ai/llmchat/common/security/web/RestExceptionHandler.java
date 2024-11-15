package ai.llmchat.common.security.web;

import ai.llmchat.common.core.exception.AuthenticationException;
import ai.llmchat.common.core.exception.DataExistsException;
import ai.llmchat.common.core.exception.PermissionException;
import ai.llmchat.common.core.exception.ServiceException;
import ai.llmchat.common.core.wrapper.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Result<?> handlerMaxUploadFile(MaxUploadSizeExceededException e) {
        log.error(e.getMessage(), e);
        return Result.badRequest(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        return Result.badRequest(e.getMessage());
    }

    @ExceptionHandler(BindException.class)
    public Result<?> bindException(BindException e) {
        log.error(e.getMessage(), e);
        return Result.badRequest(e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<?> httpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error(e.getMessage(), e);
        return Result.badRequest(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Result<?> handleAccessException(IllegalArgumentException e) {
        log.error(e.getMessage(), e);
        return Result.badRequest(e.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public Result<?> securityExceptionHandler(AuthenticationException e) {
        log.error(e.getMessage(), e);
        return Result.unauthorized(e.getMessage());
    }

    @ExceptionHandler(PermissionException.class)
    public Result<?> permissionException(PermissionException e) {
        log.error(e.getMessage(), e);
        return Result.forbidden(e.getMessage());
    }

    @ExceptionHandler(ServiceException.class)
    public Result<?> serviceExceptionHandler(ServiceException e) {
        log.error(e.getMessage(), e);
        return Result.fail(e.getMessage());
    }

    @ExceptionHandler(DataExistsException.class)
    public Result<?> dataExistsExceptionHandler(DataExistsException e) {
        log.error(e.getMessage(), e);
        return Result.fail(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result<?> defaultExceptionHandler(Exception e) {
        log.error(e.getMessage(), e);
        return Result.fail(e.getMessage());
    }
}
