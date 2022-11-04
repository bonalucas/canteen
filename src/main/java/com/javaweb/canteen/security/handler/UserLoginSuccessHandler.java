package com.javaweb.canteen.security.handler;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.javaweb.canteen.common.R;
import com.javaweb.canteen.common.ResponseUtil;
import com.javaweb.canteen.entity.MyUser;
import com.javaweb.canteen.service.MyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录成功处理类
 */
@Component
public class UserLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private MyUserService myUserService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 获取Security的实例数据
        User user = (User) authentication.getPrincipal();

        LambdaQueryWrapper<MyUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StrUtil.isNotEmpty(user.getUsername()), MyUser::getUsername, user.getUsername());

        MyUser myUser = myUserService.getOne(queryWrapper);
        // 将当前登录的用户信息存入session
        request.getSession().setAttribute("currUser", myUser);
        if (("staff").equals(myUser.getRole())) {
            ResponseUtil.out(response, R.success("员工登录成功"));
        }else{
            ResponseUtil.out(response, R.success("登录成功"));
        }
    }
}
