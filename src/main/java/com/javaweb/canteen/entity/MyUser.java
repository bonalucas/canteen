package com.javaweb.canteen.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("myUser")
public class MyUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private Long userId;

    private String name;

    private String username;

    private String password;

    private String sex;

    private String telephone;

    private String department;

    private String role;
}
