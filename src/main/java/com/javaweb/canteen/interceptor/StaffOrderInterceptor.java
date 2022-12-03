package com.javaweb.canteen.interceptor;

import cn.hutool.core.date.DateUtil;
import com.javaweb.canteen.common.R;
import com.javaweb.canteen.common.ResponseUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户点餐拦截器
 */
public class StaffOrderInterceptor implements HandlerInterceptor {
    /**
     * 用户点餐前加入拦截器，判断当前时间是否在九点之后
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取当前时刻
        String time = DateUtil.format(DateUtil.date(), "HH:mm:ss");
        // 计算当前时间大小
        int hour = Integer.parseInt(time.substring(0,2));
        int min = Integer.parseInt(time.substring(3,5));
        int sec = Integer.parseInt(time.substring(6,8));
        int target = hour * 3600 + min * 60 + sec;
        // 9点时间大小
        final int NINE = 32400;
        // 当前时间大于9点整
        if (target > NINE){
            ResponseUtil.out(response, R.fail("当前时间不允许点餐"));
            return false;
        }else{  // 当前时间不大于9点整，直接放行
            return true;
        }
    }
}
