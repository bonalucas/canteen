package com.javaweb.canteen.security;

import com.javaweb.canteen.security.filter.VerifyCodeFilter;
import com.javaweb.canteen.security.handler.*;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)   //开启方法级安全验证
public class SecurityConfig {
    /**
     * 自定义登录成功处理器
     */
    @Resource
    private UserLoginSuccessHandler userLoginSuccessHandler;
    /**
     * 自定义登录失败处理器
     */
    @Resource
    private UserLoginFailureHandler userLoginFailureHandler;
    /**
     * 自定义注销成功处理器
     */
    @Resource
    private UserLogoutSuccessHandler userLogoutSuccessHandler;
    /**
     * 自定义暂无权限处理器
     */
    @Resource
    private UserAuthAccessDeniedHandler userAuthAccessDeniedHandler;
    /**
     * 自定义未登录的处理器
     */
    @Resource
    private UserAuthenticationEntryPointHandler userAuthenticationEntryPointHandler;
    /**
     * 验证码过滤器
     */
    @Resource
    private VerifyCodeFilter verifyCodeFilter;
    /**
     * 加密方式
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    /**
     * 配置security的控制逻辑
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                // 配置不需要登录验证的请求或资源
                .antMatchers(
                        "/admin/**",
                        "/component/**",
                        "/config/**",
                        "/frontStatic/**",
                        "/back/**",
                        "/picture/**",
                        "/login",
                        "/unAuth",
                        "/login/userLogin",
                        "/common/getCode",
                        "/sale/add"
                ).permitAll()
                // 其他的都需要验证
                .anyRequest().authenticated()
            .and()
                // 配置未登录自定义处理类
                .httpBasic().authenticationEntryPoint(userAuthenticationEntryPointHandler)
            .and()
                // 配置登录地址
                .formLogin()
                    // 配置security表单登录页面地址 默认是login
                    .loginPage("/login")
                    // 配置security提交form表单请求的接口地址 默认是/login/userLogin
                    .loginProcessingUrl("/login/userLogin")
                    // 设置security提交的用户名属性值是那个 默认是username
                    .usernameParameter("username")
                    // 设置security提交的密码属性名是那个 默认是password
                    .passwordParameter("password")
                    // 登录成功跳转的请求
//                    .successForwardUrl("/success")
                    // 配置登录失败页
//                    .failureForwardUrl("/error")
                    // 配置登录成功自定义处理类
                    .successHandler(userLoginSuccessHandler)
                    // 配置登录失败自定义处理类
                    .failureHandler(userLoginFailureHandler)
            .and()
                // 配置登出地址
                .logout()
                    // 配置登出的接口地址
                    .logoutUrl("/login/logout")
                    // 配置用户登出自定义处理类
                    .logoutSuccessHandler(userLogoutSuccessHandler)
            .and()
                // 配置没有权限自定义处理类
                .exceptionHandling()
                    .accessDeniedHandler(userAuthAccessDeniedHandler)
                    // 配置没有权限返回的页面
                    .accessDeniedPage("/to403")
            .and()
                // 开启跨域
                .cors()
            .and()
                // 取消跨站请求伪造防护
                .csrf().disable();

        /* 添加验证码过滤器 */
        http.addFilterBefore(verifyCodeFilter, UsernamePasswordAuthenticationFilter.class);

        // 设置为frame页面的地址只能为同源域名下的页面
        http.headers().frameOptions().sameOrigin();

        return http.build();
    }


}
