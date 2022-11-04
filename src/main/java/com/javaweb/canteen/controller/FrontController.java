package com.javaweb.canteen.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.javaweb.canteen.entity.*;
import com.javaweb.canteen.service.BlanketOrderService;
import com.javaweb.canteen.service.MenuService;
import com.javaweb.canteen.service.OrderFormService;
import com.javaweb.canteen.service.ShopCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/front")
public class FrontController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private ShopCartService shopCartService;

    @Autowired
    private OrderFormService orderFormService;

    @Autowired
    private BlanketOrderService blanketOrderService;

    /**
     * 登出接口并跳转至登陆页面
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        SecurityContextHolder.clearContext();
        request.getSession().removeAttribute("currUser");
        return "login";
    }

    /**
     * 首页页面跳转
     */
    @GetMapping("/toMain")
    public String toMain(HttpServletRequest request){
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        // 获取当前时间的一周开始与一周结束
        Date weekOfBeginTime;
        Date weekOfEndTime;
        if (DateUtil.thisDayOfWeek() == 1) {
            weekOfBeginTime = DateUtil.beginOfDay(DateUtil.offsetDay(DateUtil.date(),(DateUtil.thisDayOfWeek()-7)));
            weekOfEndTime = DateUtil.endOfDay(DateUtil.date());
        }else{
            weekOfBeginTime = DateUtil.beginOfDay(DateUtil.offsetDay(DateUtil.date(),(2 - DateUtil.thisDayOfWeek())));
            weekOfEndTime = DateUtil.endOfDay(DateUtil.offsetDay(DateUtil.date(),(8 - DateUtil.thisDayOfWeek())));
        }

        queryWrapper.between(Menu::getCreateTime, weekOfBeginTime, weekOfEndTime)
                .orderByDesc(Menu::getCreateTime);

        List<Menu> menuList = menuService.list(queryWrapper);
        request.getSession().setAttribute("menuList", menuList);

        return "front/main";
    }

    /**
     * 菜品详情页面跳转
     */
    @GetMapping("/toDetail/{menuId}")
    public String toDetail(@PathVariable Long menuId, HttpServletRequest request){
        Menu menuDetail = menuService.getById(menuId);
        request.getSession().setAttribute("menuDetail", menuDetail);
        return "front/detail";
    }

    /**
     * 购物车页面跳转
     */
    @GetMapping("/toShopCart")
    public String toShopCart(){
        return "front/shopcart";
    }

    /**
     * 个人信息页面跳转
     */
    @GetMapping("/toInfo")
    public String toInfo() {
        return "front/information";
    }

    /**
     * 订单页面跳转
     */
    @GetMapping("/toOrder")
    public String toOrder(HttpServletRequest request) {
        MyUser currUser = (MyUser) request.getSession().getAttribute("currUser");
        Long userId = currUser.getUserId();
        // 获取所有订单
        LambdaQueryWrapper<OrderForm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderForm::getUserId, userId);
        List<OrderForm> orderList = orderFormService.list(queryWrapper);
        // 存入session
        request.getSession().setAttribute("orderList", orderList);
        // 查询所有总括订单
        List<BlanketOrder> blanketOrderList = blanketOrderService.list();
        request.getSession().setAttribute("blanketOrderList", blanketOrderList);
        return "front/list";
    }

    /**
     * 确认订单页面跳转
     */
    @GetMapping("/toOrderConfirm")
    public String toOrderConfirm(HttpServletRequest request) {
        MyUser user = (MyUser) request.getSession().getAttribute("currUser");
        Long userId = user.getUserId();
        LambdaQueryWrapper<ShopCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShopCart::getUserId, userId);
        List<ShopCart> shopCartList = shopCartService.list(queryWrapper);
        long totalPrice = 0;
        long totalWeight = 0;
        for (ShopCart sc : shopCartList){
            totalWeight += sc.getWeight();
            totalPrice += sc.getTotalPrice();
        }
        request.getSession().setAttribute("shopCartList", shopCartList);
        request.getSession().setAttribute("totalPrice", totalPrice);
        request.getSession().setAttribute("totalWeight", totalWeight);
        return "front/orderConfirm";
    }

    /**
     * 月度订单页面跳转
     */
    @GetMapping("/toMonOrder")
    public String toMonOrder(HttpServletRequest request){
        // 记录当前月份
        String monDate = DateUtil.formatDate(DateUtil.date()).substring(0, 7);
        request.getSession().setAttribute("monDate", monDate);
        MyUser currUser = (MyUser) request.getSession().getAttribute("currUser");
        Long userId = currUser.getUserId();
        // 获取时间
        String month = monDate + "-01";
        Date date = DateUtil.parse(month);
        Date begin = DateUtil.beginOfDay(date);
        Date end = DateUtil.endOfDay(DateUtil.offsetDay(DateUtil.offsetMonth(date, 1), -1));
        // 获取本月所有订单
        LambdaQueryWrapper<OrderForm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderForm::getUserId, userId)
                .between(OrderForm::getOrderTime, begin, end);
        List<OrderForm> orderList = orderFormService.list(queryWrapper);
        // 统计总金额
        Long monTotalPrice = 0L;
        // 获取本月所有订单详情
        List<BlanketOrder> monBlanketOrderList = new ArrayList<>();
        for (OrderForm orderForm : orderList){
            monTotalPrice += orderForm.getOrderPrice();
            LambdaQueryWrapper<BlanketOrder> blanketOrderLambdaQueryWrapper = new LambdaQueryWrapper<>();
            blanketOrderLambdaQueryWrapper.eq(BlanketOrder::getOrderId, orderForm.getOrderId());
            monBlanketOrderList.addAll(blanketOrderService.list(blanketOrderLambdaQueryWrapper));
        }
        request.getSession().setAttribute("monBlanketOrderList", monBlanketOrderList);
        request.getSession().setAttribute("monTotalPrice", monTotalPrice);
        return "front/monOrder";
    }
}
