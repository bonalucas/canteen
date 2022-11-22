package com.javaweb.canteen.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.javaweb.canteen.common.MyTimeUtils;
import com.javaweb.canteen.common.R;
import com.javaweb.canteen.entity.BlanketOrder;
import com.javaweb.canteen.entity.OrderForm;
import com.javaweb.canteen.entity.Sale;
import com.javaweb.canteen.service.BlanketOrderService;
import com.javaweb.canteen.service.SaleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

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
        // 通过sum(totalPrice)不存在直接单价×数量的问题
        queryWrapper.select("name, unit, sum(weight) as weight, sum(totalPrice) as totalPrice");
        queryWrapper.groupBy("name, unit, price");
        blanketOrderService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 批量打印月度销售统计
     */
    @GetMapping("/print/{saleIds}")
    public void print(@PathVariable String saleIds, HttpServletResponse response) {
        LambdaQueryWrapper<Sale> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Sale::getSaleId, Arrays.stream(saleIds.split(",")).toArray());
        List<Sale> list = saleService.list(queryWrapper);
        ServletOutputStream out = null;
        if (list.size() >= 1) {
            int cnt = 0;
            try (ExcelWriter writer = ExcelUtil.getWriterWithSheet(list.get(cnt).getMonth())) {
                for (Sale sale : list) {
                    if (cnt != 0) writer.setSheet(sale.getMonth());
                    writer.merge(3, "月度销售统计总报表");
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("统计编号", String.valueOf(sale.getSaleId()));
                    map.put("", "");
                    map.put("统计年月", sale.getMonth());
                    map.put("总计金额", sale.getTotalPrice());
                    ArrayList<Map<String, Object>> list1 = new ArrayList<>();
                    list1.add(map);
                    writer.write(list1, true);
                    String month = sale.getMonth();
                    QueryWrapper<BlanketOrder> wrapper = new QueryWrapper<>();
                    if (StrUtil.isNotEmpty(month)){
                        month = month + "-01";
                        Date begin = MyTimeUtils.getMonthOfBeginTime(month);
                        Date end = MyTimeUtils.getMonthOfEndTime(month);
                        wrapper.between("createTime", begin, end);
                    }
                    wrapper.select("name, unit, sum(weight) as weight, sum(totalPrice) as totalPrice");
                    wrapper.groupBy("name, unit, price");
                    List<BlanketOrder> blanketOrderList = blanketOrderService.list(wrapper);
                    ArrayList<Map<String, Object>> list2 = new ArrayList<>();
                    for (BlanketOrder bo : blanketOrderList) {
                        Map<String, Object> map1 = new LinkedHashMap<>();
                        map1.put("菜品名称", bo.getName());
                        map1.put("计量单位", bo.getUnit());
                        map1.put("数量", bo.getWeight());
                        map1.put("总计", bo.getTotalPrice());
                        list2.add(map1);
                    }
                    writer.write(list2, true);
                    cnt++;
                }
                response.setContentType("application/vnd.ms-excel;charset=utf-8");
                response.setHeader("Content-Disposition", "attachment;filename=saleAll.xls");
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
}
