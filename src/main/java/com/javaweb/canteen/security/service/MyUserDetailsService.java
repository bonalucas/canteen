package com.javaweb.canteen.security.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.javaweb.canteen.entity.MyUser;
import com.javaweb.canteen.service.MyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

@Component
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private MyUserService myUserService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 通过username获取用户信息
        LambdaQueryWrapper<MyUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StrUtil.isNotEmpty(username), MyUser::getUsername, username);

        MyUser user = myUserService.getOne(queryWrapper);

        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }

        // 加入用户角色
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole()));

        // 在这里做密码加密
        return new User(user.getUsername(), passwordEncoder.encode(user.getPassword()), authorities);
    }
}
