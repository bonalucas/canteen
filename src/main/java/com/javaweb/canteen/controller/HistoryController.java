package com.javaweb.canteen.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.javaweb.canteen.common.R;
import com.javaweb.canteen.entity.History;
import com.javaweb.canteen.entity.Menu;
import com.javaweb.canteen.service.HistoryService;
import com.javaweb.canteen.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Slf4j
@RestController
@RequestMapping("/history")
public class HistoryController {
    @Autowired
    private HistoryService historyService;

    @Autowired
    private MenuService menuService;


    /**
     *  历史菜单分页接口
     */
    @PreAuthorize("hasRole('manager')")
    @GetMapping("/page")
    public R<Page<History>> getAllMenu(int page, int limit,
                                    @RequestParam(required = false) String timeRange){
        Page<History> pageInfo = new Page<>(page, limit);

        LambdaQueryWrapper<History> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(StringUtils.isNotEmpty(timeRange), History::getTimeRange, timeRange)
                .orderByDesc(History::getHisId);

        historyService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 详情接口
     */
    @PreAuthorize("hasRole('manager')")
    @GetMapping("/details")
    public R<Page<Menu>> details(int page, int limit, HttpServletRequest request){
        String menuIds = (String) request.getSession().getAttribute("hisMenuIds");
        Page<Menu> pageInfo = new Page<>(page, limit);
        String[] menuArr = menuIds.split(",");
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Menu::getMenuId, Arrays.stream(menuArr).toArray());
        menuService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }
}
