package com.javaweb.canteen.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

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
            Date begin = MyTimeUtils.getMonthOfBeginTime(orderTime);
            Date end = MyTimeUtils.getMonthOfEndTime(orderTime);
            // 加入时间查找
            queryWrapper.between(OrderForm::getOrderTime, begin, end);
        }

        orderFormService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 厨师专栏订单分页接口
     */
    @GetMapping("/pageChef")
    public R<Page<BlanketOrder>> pageByChef(int page, int limit){
        Page<BlanketOrder> pageInfo = new Page<>(page, limit);

        QueryWrapper<BlanketOrder> queryWrapper = new QueryWrapper<>();
        // 获取当前这一天的开始与结尾
        Date begin = DateUtil.beginOfDay(DateUtil.date());
        Date end = DateUtil.endOfDay(DateUtil.date());
        queryWrapper.between("createTime", begin, end)
                .select("name, unit, sum(weight) as weight, sum(totalPrice) as totalPrice")
                .groupBy("name, unit, price");
        blanketOrderService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 厨师批量打印功能
     */
    @GetMapping("/printChef/{names}")
    public void printByBatchWithChef(@PathVariable String names, HttpServletResponse response){
        QueryWrapper<BlanketOrder> queryWrapper = new QueryWrapper<>();
        // 获取当前这一天的开始与结尾
        Date begin = DateUtil.beginOfDay(DateUtil.date());
        Date end = DateUtil.endOfDay(DateUtil.date());
        queryWrapper.between("createTime", begin, end)
                .in("name", Arrays.stream(names.split(",")).toArray())
                .select("name, unit, sum(weight) as weight, sum(totalPrice) as totalPrice")
                .groupBy("name, unit, price");
        List<BlanketOrder> list = blanketOrderService.list(queryWrapper);

        ArrayList<Map<String, Object>> data = new ArrayList<>();
        for (BlanketOrder bo : list) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("菜品名称", bo.getName());
            map.put("计量单位", bo.getUnit());
            map.put("数量", bo.getWeight());
            map.put("总计", bo.getTotalPrice());
            data.add(map);
        }
        ServletOutputStream out = null;
        try (ExcelWriter writer = ExcelUtil.getWriter()) {
            writer.merge(3, "今日备餐汇总");
            writer.write(data, true);
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=orderByChef.xls");
            out = response.getOutputStream();
            writer.flush(out, true);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                IoUtil.close(out);
            }
        }
    }

    /**
     * 配送员批量打印功能
     */
    @GetMapping("/printCaterer/{orderIds}")
    public void printByBatchWithCaterer(@PathVariable String orderIds, HttpServletResponse response) {
        LambdaQueryWrapper<OrderForm> queryWrapper = new LambdaQueryWrapper<>();
        Date begin = DateUtil.beginOfDay(DateUtil.date());
        Date end = DateUtil.endOfDay(DateUtil.date());
        queryWrapper.between(OrderForm::getOrderTime, begin, end)
                .in(OrderForm::getOrderId, Arrays.stream(orderIds.split(",")).toArray());
        List<OrderForm> list = orderFormService.list(queryWrapper);
        ServletOutputStream out = null;
        if (list.size() >= 1) {
            int cnt = 0;
            try (ExcelWriter writer = ExcelUtil.getWriterWithSheet(String.valueOf(list.get(cnt).getOrderId()))) {
                for (OrderForm o : list) {
                    if (cnt != 0) writer.setSheet(String.valueOf(o.getOrderId()));
                    writer.merge(5, "今日配送信息");
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("订单编号", String.valueOf(o.getOrderId()));
                    map.put("用户名", o.getName());
                    map.put("电话", o.getTelephone());
                    map.put("下单时间", DateUtil.format(o.getOrderTime(), "HH:mm:ss"));
                    map.put("", "");
                    map.put("订单金额", o.getOrderPrice());
                    ArrayList<Map<String, Object>> list1 = new ArrayList<>();
                    list1.add(map);
                    writer.write(list1, true);
                    LambdaQueryWrapper<BlanketOrder> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                    lambdaQueryWrapper.eq(BlanketOrder::getOrderId, o.getOrderId());
                    List<BlanketOrder> blanketOrderList = blanketOrderService.list(lambdaQueryWrapper);
                    ArrayList<Map<String, Object>> list2 = new ArrayList<>();
                    for (BlanketOrder bo : blanketOrderList) {
                        Map<String, Object> map1 = new LinkedHashMap<>();
                        map1.put("菜品名称", bo.getName());
                        map1.put("计量单位", bo.getUnit());
                        map1.put("数量", bo.getWeight());
                        map1.put("单价", bo.getPrice());
                        map1.put("总计", bo.getTotalPrice());
                        map1.put("下单时间", DateUtil.format(bo.getCreateTime(), "HH:mm:ss"));
                        list2.add(map1);
                    }
                    writer.write(list2, true);
                    cnt++;
                }
                response.setContentType("application/vnd.ms-excel;charset=utf-8");
                response.setHeader("Content-Disposition", "attachment;filename=orderByCaterer.xls");
                out = response.getOutputStream();
                writer.flush(out, true);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    IoUtil.close(out);
                }
            }
        }
    }


    @GetMapping("/print/{orderIds}")
    public void print(@PathVariable String orderIds, HttpServletResponse response) {
        LambdaQueryWrapper<OrderForm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(OrderForm::getOrderId, Arrays.stream(orderIds.split(",")).toArray());
        List<OrderForm> list = orderFormService.list(queryWrapper);
        ServletOutputStream out = null;
        if (list.size() >= 1) {
            int cnt = 0;
            try (ExcelWriter writer = ExcelUtil.getWriterWithSheet(String.valueOf(list.get(cnt).getOrderId()))) {
                for (OrderForm o : list) {
                    if (cnt != 0) writer.setSheet(String.valueOf(o.getOrderId()));
                    writer.merge(5, "订单信息");
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("订单编号", String.valueOf(o.getOrderId()));
                    map.put("用户名", o.getName());
                    map.put("电话", o.getTelephone());
                    map.put("下单时间", DateUtil.format(o.getOrderTime(), "HH:mm:ss"));
                    map.put("", "");
                    map.put("订单金额", o.getOrderPrice());
                    ArrayList<Map<String, Object>> list1 = new ArrayList<>();
                    list1.add(map);
                    writer.write(list1, true);
                    LambdaQueryWrapper<BlanketOrder> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                    lambdaQueryWrapper.eq(BlanketOrder::getOrderId, o.getOrderId());
                    List<BlanketOrder> blanketOrderList = blanketOrderService.list(lambdaQueryWrapper);
                    ArrayList<Map<String, Object>> list2 = new ArrayList<>();
                    for (BlanketOrder bo : blanketOrderList) {
                        Map<String, Object> map1 = new LinkedHashMap<>();
                        map1.put("菜品名称", bo.getName());
                        map1.put("计量单位", bo.getUnit());
                        map1.put("数量", bo.getWeight());
                        map1.put("单价", bo.getPrice());
                        map1.put("总计", bo.getTotalPrice());
                        map1.put("下单时间", DateUtil.format(bo.getCreateTime(), "HH:mm:ss"));
                        list2.add(map1);
                    }
                    writer.write(list2, true);
                    cnt++;
                }
                response.setContentType("application/vnd.ms-excel;charset=utf-8");
                response.setHeader("Content-Disposition", "attachment;filename=orderAll.xls");
                out = response.getOutputStream();
                writer.flush(out, true);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    IoUtil.close(out);
                }
            }
        }
    }

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
