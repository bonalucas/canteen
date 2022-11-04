package com.javaweb.canteen.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("menu")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private Long menuId;

    private String name;

    private String category;

    private String picture;

    private String unit;

    private Long price;

    private Date createTime;
}
