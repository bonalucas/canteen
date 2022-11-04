package com.javaweb.canteen.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.javaweb.canteen.common.R;
import com.javaweb.canteen.entity.Recipe;
import com.javaweb.canteen.exception.CustomException;
import com.javaweb.canteen.service.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/recipe")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    /**
     * 食谱分页接口
     */
    @PreAuthorize("hasAnyRole('chef','manager')")
    @GetMapping("/page")
    public R<Page<Recipe>> getAllRecipe(int page, int limit,
                                        @RequestParam(required = false) String name){
        Page<Recipe> pageInfo = new Page<>(page, limit);

        LambdaQueryWrapper<Recipe> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(StrUtil.isNotEmpty(name), Recipe::getName, name)
                .orderByAsc(Recipe::getRecipeId);

        recipeService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 食谱添加接口
     */
    @PreAuthorize("hasAnyRole('chef','manager')")
    @PostMapping("/add")
    public R<String> add(@RequestBody Recipe recipe){
        if (StrUtil.isEmpty(recipe.getName())) {
            throw new CustomException("名字为空!");
        }
        if (StrUtil.isEmpty(recipe.getCategory())) {
            throw new CustomException("分类为空!");
        }
        if (StrUtil.isEmpty(recipe.getUnit())) {
            throw new CustomException("计量单位为空!");
        }
        if (StrUtil.isEmpty(recipe.getDescription())) {
            throw new CustomException("描述为空!");
        }
        if (recipe.getPrice() == null) {
            throw new CustomException("价格为空!");
        }

        boolean res = recipeService.save(recipe);
        if (res) {
            return R.success("添加成功！");
        }else{
            return R.fail("添加失败！");
        }
    }

    /**
     * 食谱修改接口
     */
    @PreAuthorize("hasAnyRole('chef','manager')")
    @PutMapping("/update")
    public R<String> update(@RequestBody Recipe recipe, HttpServletRequest request){
        if (StrUtil.isEmpty(recipe.getName())) {
            throw new CustomException("名字为空!");
        }
        if (StrUtil.isEmpty(recipe.getCategory())) {
            throw new CustomException("分类为空!");
        }
        if (StrUtil.isEmpty(recipe.getUnit())) {
            throw new CustomException("计量单位为空!");
        }
        if (StrUtil.isEmpty(recipe.getDescription())) {
            throw new CustomException("描述为空!");
        }
        if (recipe.getPrice() == null) {
            throw new CustomException("价格为空!");
        }
        Recipe editRecipe = (Recipe) request.getSession().getAttribute("editRecipe");
        if (StrUtil.isEmpty(recipe.getPicture())){
            recipe.setPicture(editRecipe.getPicture());
        }
        Long recipeId = editRecipe.getRecipeId();
        LambdaQueryWrapper<Recipe> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Recipe::getRecipeId, recipeId);
        boolean res = recipeService.update(recipe, lambdaQueryWrapper);
        if (res) {
            return R.success("修改成功！");
        }else{
            return R.fail("修改失败！");
        }
    }

    /**
     * 删除食谱接口
     */
    @PreAuthorize("hasAnyRole('chef','manager')")
    @DeleteMapping("/delete/{recipeId}")
    public R<String> delete(@PathVariable Long recipeId){
        boolean res = false;
        if (recipeId != null) {
            res = recipeService.removeById(recipeId);
        }
        if (res) {
            return R.success("删除成功！");
        }else{
            return R.fail("删除失败！");
        }
    }

    /**
     * 批量删除食谱接口
     */
    @PreAuthorize("hasAnyRole('chef','manager')")
    @DeleteMapping("/deleteBatch/{recipeIds}")
    public R<String> deleteBatchRecipeIds(@PathVariable String recipeIds){
        boolean res = true;
        String[] ids = recipeIds.split(",");
        for (String id : ids) {
            Long recipeId = Long.valueOf(id);
            boolean flag = recipeService.removeById(recipeId);
            res = res && flag;
        }
        if (res) {
            return R.success("批量删除成功");
        }else{
            return R.fail("批量删除失败");
        }
    }
}
