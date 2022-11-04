package com.javaweb.canteen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.javaweb.canteen.entity.BlanketOrder;
import com.javaweb.canteen.mapper.BlanketOrderMapper;
import com.javaweb.canteen.service.BlanketOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BlanketOrderServiceImpl extends ServiceImpl<BlanketOrderMapper, BlanketOrder> implements BlanketOrderService {
}
