package com.javaweb.canteen.security.filter;

import com.javaweb.canteen.exception.VerifyCodeException;
import com.javaweb.canteen.security.handler.UserLoginFailureHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证码校验过滤器
 */
@Slf4j
@Component
public class VerifyCodeFilter extends OncePerRequestFilter {

    @Resource
    private UserLoginFailureHandler userLoginFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getRequestURI().equals("/login/userLogin") && request.getMethod().equalsIgnoreCase("post")){
            try {
                validate(request);
            } catch (VerifyCodeException e) {
                userLoginFailureHandler.onAuthenticationFailure(request,response,e);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private void validate(HttpServletRequest request) throws ServletRequestBindingException {
        String captcha = request.getParameter("captcha");
        String code = (String) request.getSession().getAttribute("code");
        log.info("获取提交的code={}",captcha);
        log.info("获取保存的code={}",code);
        if(!code.equalsIgnoreCase(captcha)){
            throw new VerifyCodeException("验证码不正确");
        }
        request.getSession().removeAttribute("code");
    }
}
