package com.javaweb.canteen.security.handler;

import com.javaweb.canteen.common.R;
import com.javaweb.canteen.common.ResponseUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登出成功处理类
 */
@Component
public class UserLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 清理登录用户信息
        SecurityContextHolder.clearContext();
        request.getSession().removeAttribute("currUser");
        ResponseUtil.out(response, R.success("注销成功"));
    }
}
