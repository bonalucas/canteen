package com.javaweb.canteen.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("orderForm")
public class OrderForm implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private Long orderId;

    private Long userId;

    private String name;

    private String telephone;

    private Date orderTime;

    private Long orderPrice;
}
