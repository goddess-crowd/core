package com.goddess.common.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goddess.common.constant.GoddessEnum;
import com.goddess.common.web.ResultResponse;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;


/**
 * 异常处理器
 * @author 失败女神
 * @email: 18733123202@163.com
 * @date 2021/4/7 上午9:35
 * @Copyright © 女神帮
 */
@RestControllerAdvice
public class AppExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Value("${spring.application.name:center}")
    private String serve;

    public AppExceptionHandler(String serve) {
        this.serve = serve;
    }

    public String getServe() {
        return serve;
    }

    public AppExceptionHandler(){
    }
    /**
     * 处理自定义异常
     */
    @ExceptionHandler(AppException.class)
    public ResultResponse handleRRException(AppException e) {
        logger.error(e.getMessage(), e);
        return ResultResponse.error(e.getCode(), e.getMsg());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResultResponse handlerNoFoundException(Exception e) {
        logger.error(e.getMessage(), e);
        return ResultResponse.error( serve + ".404", "路径不存在，请检查路径是否正确");
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResultResponse handleDuplicateKeyException(DuplicateKeyException e) {
        logger.error(e.getMessage(), e);
        return ResultResponse.error(serve + "." + ErrorCode.DUPLICATION, "数据库中已存在该记录");
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResultResponse handleAuthorizationException(AuthorizationException e) {
        logger.error(e.getMessage(), e);

        return ResultResponse.error( serve + ".401", "没有权限，请联系管理员授权");
    }

    @ExceptionHandler(Exception.class)
    public ResultResponse handleException(Exception e) {
        logger.error(e.getMessage(), e);
        return ResultResponse.error( serve + ".500", "未知错误，请联系系统管理员");
    }

    /**
     * 处理请求对象属性不满足校验规则的异常信息
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResultResponse<Void> exception(HttpServletRequest request, MethodArgumentNotValidException exception) {
        BindingResult result = exception.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();
        List<ValidMsg> errorList = new ArrayList<>();
        for (FieldError error : fieldErrors) {
            errorList.add(new ValidMsg(error.getObjectName(), error.getField(), error.getDefaultMessage()));
        }
        logger.error(exception.getMessage(), exception);
        return new ResultResponse<Void>(serve + "." + ErrorCode.DATA_ILLEGAL, listToJson(errorList));
    }

    /**
     * 参数类型数据异常
     */
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResultResponse<Void> exception(HttpServletRequest request, MethodArgumentTypeMismatchException exception) {
        logger.error(exception.getMessage(), exception);
        List<ValidMsg> errorList = new ArrayList<>();
        errorList.add(new ValidMsg("", exception.getName(), exception.getMessage()));
        return new ResultResponse<Void>(serve + "." + ErrorCode.DATA_ILLEGAL, listToJson(errorList));
    }

    /**
     * 丢失参数异常
     */
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResultResponse<Void> exception(HttpServletRequest request, MissingServletRequestParameterException exception) {
        List<ValidMsg> errorList = new ArrayList<>();
        errorList.add(new ValidMsg("", exception.getParameterName(), exception.getMessage()));
        logger.error(exception.getMessage(), exception);
        return new ResultResponse<Void>( serve + "." + ErrorCode.DATA_MISSING, listToJson(errorList));
    }

    /**
     * 处理请求单个参数不满足校验规则的异常信息
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResultResponse<Void> constraintViolationExceptionHandler(HttpServletRequest request,
                                                                    ConstraintViolationException exception) {
        logger.error(exception.getMessage(), exception);
        return ResultResponse.error( serve + "." + ErrorCode.DATA_ILLEGAL, exception.getMessage());
    }

    /**
     * 参数无效的异常信息处理。
     */
    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResultResponse<Void> handleIllegalArgumentException(HttpServletRequest request,
                                                               IllegalArgumentException exception) {
        logger.error(exception.getMessage(), exception);
        return ResultResponse.error(serve + "." + ErrorCode.DATA_ILLEGAL, exception.getMessage());
    }

    /**
     * 消息不可读异常
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResultResponse<Void> handleIllegalArgumentException(HttpServletRequest request,
                                                               HttpMessageNotReadableException exception) {
        logger.error(exception.getMessage(), exception);
        return ResultResponse.error( serve + "." + ErrorCode.DATA_ILLEGAL, exception.getMessage());
    }

    private String listToJson(List<ValidMsg> errorList) {

        String msg = "数据验证错误";
        try {
            msg = new ObjectMapper().writeValueAsString(errorList);

        } catch (JsonProcessingException e) {
        }
        return msg;
    }
}
