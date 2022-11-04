package com.javaweb.canteen.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一封装结果类
 */
@Data
public class R<T> implements Serializable {
    private Integer code;
    private String msg;
    private T data;

    public static <T> R<T> success(int code, String msg, T data) {
        R<T> result = new R<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public static <T> R<T> fail(int code, String msg, T data) {
        R<T> result = new R<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public static <T> R<T> success(T data) {
        return success(200, "操作成功", data);
    }

    public static <T> R<T> fail(String msg) {
        return fail(500, msg, null);
    }
}
