package com.javaweb.canteen.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 自定义验证码异常
 */
public class VerifyCodeException extends AuthenticationException {
    public VerifyCodeException(String msg) {
        super(msg);
    }
}
