package com.javaweb.canteen.interceptor;

import cn.hutool.core.date.DateUtil;
import com.javaweb.canteen.common.R;
import com.javaweb.canteen.common.ResponseUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ManagerMenuInterceptor implements HandlerInterceptor {
    /**
     * 食堂经理在周一的7点之前挑选本周菜单，其余时间无法挑选
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取当前是本周第几天
        int dayOfWeek = DateUtil.thisDayOfWeek();
        // 判断是否是周一
        if (dayOfWeek != 2){
            ResponseUtil.out(response, R.fail("当前时间无法挑选本周菜单"));
            return false;
        }
        // 获取当前时刻
        String time = DateUtil.format(DateUtil.date(), "HH:mm:ss");
        // 计算当前时间大小
        int hour = Integer.parseInt(time.substring(0,2));
        int min = Integer.parseInt(time.substring(3,5));
        int sec = Integer.parseInt(time.substring(6,8));
        int target = hour * 3600 + min * 60 + sec;
        // 7点时间大小
        final int SEVEN = 25200;
        // 判断是否在7点前
        if (target > SEVEN){
            ResponseUtil.out(response, R.fail("当前时间无法挑选本周菜单"));
            return false;
        }else{  //在七点前则放行
            return true;
        }
    }
}
