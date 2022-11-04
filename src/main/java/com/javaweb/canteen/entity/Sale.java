package com.javaweb.canteen.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("sale")
public class Sale implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private Long saleId;

    private String month;

    private Long totalPrice;
}
