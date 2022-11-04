package com.javaweb.canteen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.javaweb.canteen.entity.ShopCart;
import com.javaweb.canteen.mapper.ShopCartMapper;
import com.javaweb.canteen.service.ShopCartService;
import org.springframework.stereotype.Service;

@Service
public class ShopCartServiceImpl extends ServiceImpl<ShopCartMapper, ShopCart> implements ShopCartService {
}
