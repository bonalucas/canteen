package com.javaweb.canteen.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.javaweb.canteen.common.MyTimeUtils;
import com.javaweb.canteen.common.R;
import com.javaweb.canteen.entity.BlanketOrder;
import com.javaweb.canteen.entity.Sale;
import com.javaweb.canteen.service.BlanketOrderService;
import com.javaweb.canteen.service.SaleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
@Slf4j
@RestController
@RequestMapping("/sale")
public class SaleController {

    @Autowired
    private SaleService saleService;

    @Autowired
    private BlanketOrderService blanketOrderService;

    /**
     * 销售分页接口
     */
    @PreAuthorize("hasAnyRole('treasurer','manager')")
    @GetMapping("/page")
    public R<Page<Sale>> page(int page, int limit,
                              @RequestParam(required = false) String month){

        Page<Sale> pageInfo = new Page<>(page, limit);

        LambdaQueryWrapper<Sale> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StrUtil.isNotEmpty(month), Sale::getMonth, month);

        saleService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 销售订单详情接口
     */
    @PreAuthorize("hasAnyRole('treasurer','manager')")
    @GetMapping("/details")
    public R<Page<BlanketOrder>> details(int page, int limit, HttpServletRequest request){
        String month = (String) request.getSession().getAttribute("month");
        Page<BlanketOrder> pageInfo = new Page<>(page, limit);

        QueryWrapper<BlanketOrder> queryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotEmpty(month)){
            month = month + "-01";
            Date begin = MyTimeUtils.getMonthOfBeginTime(month);
            Date end = MyTimeUtils.getMonthOfEndTime(month);
            queryWrapper.between("createTime", begin, end);
        }
        queryWrapper.select("name, unit, sum(weight) as weight, price, sum(totalPrice) as totalPrice");
        queryWrapper.groupBy("name, unit, price");
        blanketOrderService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }
}
