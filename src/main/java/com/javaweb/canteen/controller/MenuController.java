package com.javaweb.canteen.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.javaweb.canteen.common.R;
import com.javaweb.canteen.entity.Menu;
import com.javaweb.canteen.entity.Recipe;
import com.javaweb.canteen.service.MenuService;
import com.javaweb.canteen.service.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private RecipeService recipeService;

    /**
     *  本周菜单分页接口
     */
    @GetMapping("/page")
    public R<Page<Menu>> getWeekMenu(int page, int limit,
                                      @RequestParam(required = false) String name){
        Page<Menu> pageInfo = new Page<>(page, limit);

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

        queryWrapper.eq(StringUtils.isNotEmpty(name), Menu::getName, name)
                .between(Menu::getCreateTime, weekOfBeginTime, weekOfEndTime)
                .orderByDesc(Menu::getCreateTime);

        menuService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    /**
     *  历史菜单分页接口
     */
    @PreAuthorize("hasRole('manager')")
    @GetMapping("/pageAll")
    public R<Page<Menu>> getAllMenu(int page, int limit,
                                     @RequestParam(required = false) String name){
        Page<Menu> pageInfo = new Page<>(page, limit);

        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(StringUtils.isNotEmpty(name), Menu::getName, name)
                .orderByDesc(Menu::getCreateTime);

        menuService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    /**
     *  添加菜单接口
     */
    @PreAuthorize("hasRole('manager')")
    @PostMapping("/add/{recipeId}")
    public R<String> add(@PathVariable Long recipeId){
        Recipe recipe = recipeService.getById(recipeId);
        String name = recipe.getName();
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
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getName, name).between(Menu::getCreateTime, weekOfBeginTime, weekOfEndTime);
        Menu isExist = menuService.getOne(queryWrapper);
        boolean res;
        if (isExist == null){
            Menu menu = new Menu();
            menu.setName(recipe.getName());
            menu.setCategory(recipe.getCategory());
            menu.setPicture(recipe.getPicture());
            menu.setUnit(recipe.getUnit());
            menu.setPrice(recipe.getPrice());
            menu.setCreateTime(DateUtil.date());
            res = menuService.save(menu);
        }else{
            return R.fail("当前菜品已被添加");
        }
        if (res) {
            return R.success("添加成功");
        }else{
            return R.fail("添加失败");
        }
    }

    /**
     *  删除菜单接口
     */
    @PreAuthorize("hasRole('manager')")
    @DeleteMapping("/delete/{menuId}")
    public R<String> delete(@PathVariable Long menuId){
        boolean res = false;
        if (menuId != null) {
            res = menuService.removeById(menuId);
        }
        if (res) {
            return R.success("删除成功");
        }else{
            return R.fail("删除失败");
        }
    }

    /**
     *  批量删除菜单接口
     */
    @PreAuthorize("hasRole('manager')")
    @DeleteMapping("/deleteBatch/{menuIds}")
    public R<String> deleteBatch(@PathVariable String menuIds){
        boolean res = true;
        String[] ids = menuIds.split(",");
        for (String id : ids) {
            Long menuId = Long.valueOf(id);
            boolean flag = menuService.removeById(menuId);
            res = res && flag;
        }
        if (res) {
            return R.success("批量删除成功");
        }else{
            return R.fail("批量删除失败");
        }
    }

    /**
     *  查询某个菜单接口
     */
    @GetMapping("{menuId}")
    public R<Menu> getMenuInfo(@PathVariable Long menuId){
        Menu menu = menuService.getById(menuId);

        if (menu != null) {
            return R.success(menu);
        }else{
            return R.fail("获取菜品信息失败");
        }
    }
}
