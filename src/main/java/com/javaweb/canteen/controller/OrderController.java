package com.javaweb.canteen.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.javaweb.canteen.common.MyTimeUtils;
import com.javaweb.canteen.common.R;
import com.javaweb.canteen.entity.BlanketOrder;
import com.javaweb.canteen.entity.MyUser;
import com.javaweb.canteen.entity.OrderForm;
import com.javaweb.canteen.entity.ShopCart;
import com.javaweb.canteen.exception.CustomException;
import com.javaweb.canteen.service.BlanketOrderService;
import com.javaweb.canteen.service.OrderFormService;
import com.javaweb.canteen.service.ShopCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private ShopCartService shopCartService;

    @Autowired
    private OrderFormService orderFormService;

    @Autowired
    private BlanketOrderService blanketOrderService;

    /**
     * 订单提交接口
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody OrderForm orderForm, HttpServletRequest request){
        // 获取当前用户的id
        MyUser currUser = (MyUser) request.getSession().getAttribute("currUser");
        Long userId = currUser.getUserId();

        // 获取当前用户的购物车信息
        LambdaQueryWrapper<ShopCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShopCart::getUserId, userId);
        List<ShopCart> shopCartList = shopCartService.list(queryWrapper);

        // 记录当前时间
        Date now = DateUtil.date();

        // 补齐订单信息
        // 定制id
        long orderId = RandomUtil.randomInt(1000, Integer.MAX_VALUE);
        orderForm.setOrderId(orderId);
        orderForm.setUserId(userId);
        orderForm.setName(currUser.getUsername());
        orderForm.setOrderTime(now);
        orderForm.setTelephone(currUser.getTelephone());

        // 将订单信息加入订单中并将购物车信息加入总括订单
        boolean res = orderFormService.save(orderForm);
        if (!res) throw new CustomException("订单加入失败");

        for (ShopCart sc : shopCartList) {
            BlanketOrder blanketOrder = new BlanketOrder();
            blanketOrder.setName(sc.getName());
            blanketOrder.setUnit(sc.getUnit());
            blanketOrder.setWeight(sc.getWeight());
            blanketOrder.setPrice(sc.getPrice());
            blanketOrder.setTotalPrice(sc.getTotalPrice());
            blanketOrder.setOrderId(orderId);
            blanketOrder.setCreateTime(now);
            boolean ans = blanketOrderService.save(blanketOrder);
            if (!ans) throw new CustomException("订单加入失败");
        }

        // 清空当前用户的购物车
        LambdaQueryWrapper<ShopCart> shopCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shopCartLambdaQueryWrapper.eq(ShopCart::getUserId, userId);
        boolean b = shopCartService.remove(shopCartLambdaQueryWrapper);

        if (b) {
            return R.success("订单提交成功");
        }else {
            return R.fail("订单提交成功，购物车清空失败");
        }
    }

    /**
     * 订单分页接口
     */
    @GetMapping("/page")
    public R<Page<OrderForm>> page(int page, int limit,
                                   @RequestParam(required = false) String name,
                                   @RequestParam(required = false) String orderTime){
        Page<OrderForm> pageInfo = new Page<>(page, limit);

        LambdaQueryWrapper<OrderForm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StrUtil.isNotEmpty(name), OrderForm::getName, name)
                .orderByDesc(OrderForm::getOrderTime);

        // 处理当前这一天的订单
        if (orderTime != null && StrUtil.isNotEmpty(orderTime)) {
            Date begin = MyTimeUtils.getDayBeginOfTime(orderTime);
            Date end = MyTimeUtils.getDayEndOfTime(orderTime);
            // 加入时间查找
            queryWrapper.between(OrderForm::getOrderTime, begin, end);
        }

        orderFormService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 订单状态更改接口
     */
//    @PreAuthorize("hasAnyRole('chef','caterer')")
//    @PutMapping("/updateState/{state}/{orderId}")
//    public R<String> updateState(@PathVariable Long orderId, @PathVariable Integer state){
//        LambdaUpdateWrapper<OrderForm> updateWrapper = new LambdaUpdateWrapper<>();
//        updateWrapper.eq(orderId != null, OrderForm::getOrderId, orderId)
//                .set(OrderForm::getState, state + 1);
//        boolean res = orderFormService.update(null, updateWrapper);
//        if (res) {
//            return R.success("状态更新成功");
//        }else{
//            return R.fail("状态更新失败");
//        }
//    }

    /**
     * 订单详情接口
     */
    @GetMapping("/boPage")
    public R<Page<BlanketOrder>> boPage(int page, int limit, HttpServletRequest request){
        Page<BlanketOrder> pageInfo = new Page<>(page, limit);

        Long orderId = (Long) request.getSession().getAttribute("detailsId");

        LambdaQueryWrapper<BlanketOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(orderId != null, BlanketOrder::getOrderId, orderId);
        blanketOrderService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }
}
