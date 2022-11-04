package com.javaweb.canteen.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.javaweb.canteen.entity.BlanketOrder;
import com.javaweb.canteen.entity.Notify;
import com.javaweb.canteen.entity.Sale;
import com.javaweb.canteen.entity.ShopCart;
import com.javaweb.canteen.service.BlanketOrderService;
import com.javaweb.canteen.service.NotifyService;
import com.javaweb.canteen.service.SaleService;
import com.javaweb.canteen.service.ShopCartService;
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
    private NotifyService notifyService;

    @Autowired
    private ShopCartService shopCartService;

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
            Date date = DateUtil.parse(month);
            Date begin = DateUtil.beginOfDay(date);
            Date end = DateUtil.endOfDay(DateUtil.offsetDay(DateUtil.offsetMonth(date, 1), -1));
            queryWrapper.between(BlanketOrder::getCreateTime, begin, end);
        }
        List<BlanketOrder> list = blanketOrderService.list(queryWrapper);
        for (BlanketOrder bo : list) {
            totalPrice += bo.getTotalPrice();
        }
        sale.setTotalPrice(totalPrice);

        boolean res = saleService.save(sale);
        if (res){
            Notify notify = new Notify();
            notify.setMessage("月度销售详情已生成，请前往月度销售中查看");
            notify.setCreateTime(DateUtil.date());
            notifyService.save(notify);
        }
    }

    /**
     * 每天的11点30分会通知配送员开始配送
     */
    @Scheduled(cron = "0 30 11 * * ?")    // 执行时间为每日的11点30分
    public void notifyDelivery(){
        Notify notify = new Notify();
        notify.setMessage("订单已完成出餐，请配送员前往订单进度->正在配送中查看并配送");
        notify.setCreateTime(DateUtil.date());
        notifyService.save(notify);
    }

    /**
     * 每天的9点整会通知食堂大厨开始备餐
     */
    @Scheduled(cron = "0 0 9 * * ?")
    public void notifyMeal(){
        Notify notify = new Notify();
        notify.setMessage("员工已全部发出订单，请食堂大厨前往订单进度->正在出餐中查看并备餐");
        notify.setCreateTime(DateUtil.date());
        notifyService.save(notify);
    }

    /**
     * 每周的周一的0点整会清除所有用户的购物车信息
     */
    @Scheduled(cron = "0 0 0 ? * 2")
    public void clearShopCart(){
        LambdaQueryWrapper<ShopCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.or();
        shopCartService.remove(queryWrapper);
    }
}
