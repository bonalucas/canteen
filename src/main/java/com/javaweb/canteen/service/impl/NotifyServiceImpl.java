package com.javaweb.canteen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.javaweb.canteen.entity.Notify;
import com.javaweb.canteen.mapper.NotifyMapper;
import com.javaweb.canteen.service.NotifyService;
import org.springframework.stereotype.Service;

@Service
public class NotifyServiceImpl extends ServiceImpl<NotifyMapper, Notify> implements NotifyService {
}
