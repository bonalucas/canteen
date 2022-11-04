package com.javaweb.canteen.security.handler;

import com.javaweb.canteen.common.R;
import com.javaweb.canteen.common.ResponseUtil;
import com.javaweb.canteen.exception.CustomException;
import com.javaweb.canteen.exception.VerifyCodeException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录失败处理类
 */
@Component
public class UserLoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // 这些对于操作的处理类可以根据不同异常进行不同处理
        if (exception instanceof VerifyCodeException){
            ResponseUtil.out(response, R.fail(500, "验证码错误", null));
        }
        if (exception instanceof UsernameNotFoundException){
            ResponseUtil.out(response, R.fail(500, "用户名不存在", null));
        }
        if (exception instanceof BadCredentialsException){
            ResponseUtil.out(response,R.fail(500, "用户名密码不正确", null));
        }
        ResponseUtil.out(response,R.fail(500, "出现未知错误，登录失败", null));
    }
}
