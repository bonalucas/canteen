package com.javaweb.canteen.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.javaweb.canteen.common.MyTimeUtils;
import com.javaweb.canteen.entity.*;
import com.javaweb.canteen.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 配置定时任务
 */
@Slf4j
@Component
public class MyScheduleTask {
    @Autowired
    private SaleService saleService;

    @Autowired
    private BlanketOrderService blanketOrderService;

    @Autowired
    private ShopCartService shopCartService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private HistoryService historyService;

    /**
     * 每月的1号会自动生成月度销售信息
     */
    @Scheduled(cron = "0 0 0 1 * ?")    // 执行时间为每月的1号的00:00:00
    public void addMonthSale(){
        // 构建销售对象
        Sale sale = new Sale();
        // 获取对应年月
        Date currDate = DateUtil.lastMonth();
        String month = DateUtil.formatDate(currDate).substring(0,7);
        sale.setMonth(month);

        // 查询这个月总金额
        Long totalPrice = 0L;
        LambdaQueryWrapper<BlanketOrder> queryWrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotEmpty(month)){
            month = month + "-01";
            Date begin = MyTimeUtils.getMonthOfBeginTime(month);
            Date end = MyTimeUtils.getMonthOfEndTime(month);
            queryWrapper.between(BlanketOrder::getCreateTime, begin, end);
        }
        List<BlanketOrder> list = blanketOrderService.list(queryWrapper);
        for (BlanketOrder bo : list) {
            totalPrice += bo.getTotalPrice();
        }
        sale.setTotalPrice(totalPrice);

        boolean res = saleService.save(sale);
        log.info("上个月的销售订单自动生成{}", res ? "成功" : "失败");
    }

    /**
     * 每周的周一的0点整自动清除所有用户的购物车信息
     */
    @Scheduled(cron = "0 0 0 ? * 2")
    public void clearShopCart(){
        LambdaQueryWrapper<ShopCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.or();
        shopCartService.remove(queryWrapper);
        log.info("本周已结束，自动清除所有用户购物车");
    }

    /**
     * 每周周天23:00:00分自动统计该周的所有菜品并归纳为这周历史菜单
     */
    @Scheduled(cron = "0 0 23 ? * 1")
    public void addHistoryMenu() {
        History history = new History();
        // 获取当前时间的一周的开始与结尾
        Date weekOfBeginTime = MyTimeUtils.getWeekOfBeginTime();
        Date weekOfEndTime = MyTimeUtils.getWeekOfEndTime();
        String weekOfBeginTimeStr = DateUtil.formatDate(weekOfBeginTime);
        String weekOfEndTimeStr = DateUtil.formatDate(weekOfEndTime);
        history.setTimeRange(weekOfBeginTimeStr + "~" + weekOfEndTimeStr);
        StringBuilder sb = new StringBuilder();
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.between(Menu::getCreateTime, weekOfBeginTime, weekOfEndTime);
        List<Menu> menuList = menuService.list(queryWrapper);
        for (Menu m : menuList) {
            sb.append(m.getMenuId()).append(",");
        }
        history.setMenuIds(sb.substring(0, sb.length() - 1));
        boolean res = historyService.save(history);
        log.info("自动收集历史菜单：{}", res ? "成功" : "失败");
    }
}
