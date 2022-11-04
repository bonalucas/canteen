package com.javaweb.canteen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.javaweb.canteen.entity.Recipe;
import com.javaweb.canteen.mapper.RecipeMapper;
import com.javaweb.canteen.service.RecipeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RecipeServiceImpl extends ServiceImpl<RecipeMapper, Recipe> implements RecipeService {
}
