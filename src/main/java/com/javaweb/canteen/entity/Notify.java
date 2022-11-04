package com.javaweb.canteen.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("notify")
public class Notify implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private Long notifyId;

    private String message;

    private Date createTime;
}
