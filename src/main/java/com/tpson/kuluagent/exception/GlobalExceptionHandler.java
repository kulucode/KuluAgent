package com.tpson.kuluagent.exception;

import com.tpson.kuluagent.VO.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Zhangka in 2018/04/11
 */
@ControllerAdvice
class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    public static final String DEFAULT_ERROR_VIEW = "error";

    @ExceptionHandler(value = ParamRuntimeException.class)
    @ResponseBody
    public ResultVO baseErrorHandler(HttpServletRequest req, Exception e) {
        LOGGER.error("参数错误", e);
        return ResultVO.failResult(e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultVO defaultErrorHandler(Exception e) {
        LOGGER.error("出错了", e);
        return ResultVO.failResult(e.getMessage());
    }
}
