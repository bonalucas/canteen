package com.javaweb.canteen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.javaweb.canteen.entity.Recipe;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RecipeMapper extends BaseMapper<Recipe> {
}
