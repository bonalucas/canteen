package com.javaweb.canteen.exception;

import com.javaweb.canteen.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 全局异常处理
 */
@Slf4j
@ResponseBody
@ControllerAdvice(annotations = {RestController.class, Controller.class})
public class GlobalExceptionHandler {
    /**
     * 自定义业务异常处理方法
     * @param ex 异常信息
     * @return R
     */
    @ExceptionHandler(CustomException.class)
    public R<String> customException(CustomException ex){
        log.info(ex.getMessage());
        return R.fail(ex.getMessage());
    }
}
