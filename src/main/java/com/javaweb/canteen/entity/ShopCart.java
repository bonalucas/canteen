package com.javaweb.canteen.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("shopCart")
public class ShopCart implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private Integer scId;

    private String name;

    private String unit;

    private Integer weight;

    private Long price;

    private Long totalPrice;

    private String picture;

    private Long userId;
}
