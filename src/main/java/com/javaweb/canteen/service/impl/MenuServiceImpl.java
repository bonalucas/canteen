package com.javaweb.canteen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.javaweb.canteen.entity.Menu;
import com.javaweb.canteen.mapper.MenuMapper;
import com.javaweb.canteen.service.MenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
}
