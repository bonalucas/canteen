package com.javaweb.canteen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.javaweb.canteen.entity.OrderForm;
import com.javaweb.canteen.mapper.OrderFormMapper;
import com.javaweb.canteen.service.OrderFormService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderFormServiceImpl extends ServiceImpl<OrderFormMapper, OrderForm> implements OrderFormService {
}
