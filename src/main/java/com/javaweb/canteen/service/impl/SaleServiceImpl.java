package com.javaweb.canteen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.javaweb.canteen.entity.Sale;
import com.javaweb.canteen.mapper.SaleMapper;
import com.javaweb.canteen.service.SaleService;
import org.springframework.stereotype.Service;

@Service
public class SaleServiceImpl extends ServiceImpl<SaleMapper, Sale> implements SaleService {
}
