package com.javaweb.canteen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.javaweb.canteen.entity.MyUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MyUserMapper extends BaseMapper<MyUser> {
}
