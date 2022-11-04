package com.javaweb.canteen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.javaweb.canteen.entity.MyUser;
import com.javaweb.canteen.mapper.MyUserMapper;
import com.javaweb.canteen.service.MyUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MyUserServiceImpl extends ServiceImpl<MyUserMapper, MyUser> implements MyUserService {
}
