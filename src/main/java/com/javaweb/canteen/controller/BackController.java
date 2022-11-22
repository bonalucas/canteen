package com.javaweb.canteen.controller;

import com.javaweb.canteen.entity.Menu;
import com.javaweb.canteen.entity.MyUser;
import com.javaweb.canteen.entity.Recipe;
import com.javaweb.canteen.service.MenuService;
import com.javaweb.canteen.service.MyUserService;
import com.javaweb.canteen.service.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
public class BackController {

    @Autowired
    private MyUserService myUserService;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private MenuService menuService;

    /**
     * 跳转用户管理页面
     */
    @PreAuthorize("hasRole('manager')")
    @GetMapping("/back/toUser")
    public String toUser(){
        return "user/user";
    }

    /**
     * 跳转食谱管理页面
     */
    @PreAuthorize("hasAnyRole('chef','manager')")
    @GetMapping("/back/toRecipe")
    public String toRecipe(){
        return "recipe/recipe";
    }

    /**
     * 修改用户信息页面跳转
     */
    @PreAuthorize("hasRole('manager')")
    @GetMapping("/back/toUserEdit/{userId}")
    public String toUserEdit(@PathVariable Long userId, HttpServletRequest request){
        // 根据用户Id查询用户信息
        MyUser user = myUserService.getById(userId);
        request.getSession().removeAttribute("editUser");
        request.getSession().setAttribute("editUser", user);
        return "user/edit";
    }

    /**
     * 修改食谱信息页面跳转
     */
    @PreAuthorize("hasAnyRole('chef','manager')")
    @GetMapping("/back/toRecipeEdit/{recipeId}")
    public String toRecipeEdit(@PathVariable Long recipeId, HttpServletRequest request){
        // 根据食谱Id查询对应食谱信息
        Recipe recipe = recipeService.getById(recipeId);
        request.getSession().removeAttribute("editRecipe");
        request.getSession().setAttribute("editRecipe", recipe);
        return "recipe/edit";
    }

    /**
     * 挑选下周菜单页面跳转
     */
    @PreAuthorize("hasRole('manager')")
    @GetMapping("/back/toAddMenu")
    public String toAddMenu(){
        return "menu/addMenu";
    }

    /**
     * 本周菜单页面跳转
     */
    @PreAuthorize("hasRole('manager')")
    @GetMapping("/back/toMenu")
    public String toMenu(){
        return "menu/menu";
    }

    /**
     * 修改菜单信息页面跳转
     */
    @PreAuthorize("hasRole('manager')")
    @GetMapping("/back/toMenuEdit/{menuId}")
    public String toMenuEdit(@PathVariable Long menuId, HttpServletRequest request){
        // 根据食谱Id查询对应食谱信息
        Menu menu = menuService.getById(menuId);
        request.getSession().removeAttribute("editMenu");
        request.getSession().setAttribute("editMenu", menu);
        return "menu/edit";
    }

    /**
     * 下周菜单页面跳转
     */
    @PreAuthorize("hasRole('manager')")
    @GetMapping("/back/toNextMenu")
    public String toNextMenu(){
        return "menu/nextMenu";
    }

    /**
     * 历史菜单页面跳转
     */
    @PreAuthorize("hasRole('manager')")
    @GetMapping("/back/toHistoryMenu")
    public String toHistoryMenu(){
        return "menu/historyMenu";
    }

    /**
     * 历史详情页面跳转
     */
    @GetMapping("/back/toHistoryDetail/{menuIds}")
    public String toHistoryDetail(@PathVariable String menuIds, HttpServletRequest request){
        request.getSession().setAttribute("hisMenuIds", menuIds);
        return "menu/details";
    }

    /**
     * 正在出餐订单页面跳转
     */
    @PreAuthorize("hasRole('chef')")
    @GetMapping("/back/toNoMeal")
    public String toNoMeal(){
        return "order/noMeal";
    }

    /**
     * 正在配送订单页面跳转
     */
    @PreAuthorize("hasRole('caterer')")
    @GetMapping("/back/toUndelivered")
    public String toUndelivered(){
        return "order/undelivered";
    }

    /**
     * 已完成订单页面跳转
     */
    @PreAuthorize("hasAnyRole('treasurer','manager')")
    @GetMapping("/back/toCompleted")
    public String toCompleted(){
        return "order/completed";
    }

    /**
     * 订单详情页面跳转
     */
    @GetMapping("/back/toDetails/{orderId}")
    public String toDetails(@PathVariable Long orderId, HttpServletRequest request){
        request.getSession().setAttribute("detailsId", orderId);
        return "order/details";
    }

    /**
     * 月度销售页面跳转
     */
    @PreAuthorize("hasAnyRole('treasurer','manager')")
    @GetMapping("/back/toSale")
    public String toSale(){
        return "sale/sale";
    }

    /**
     * 月度销售详情页面跳转
     */
    @GetMapping("/back/toSaleDetails/{month}")
    public String toSaleDetails(@PathVariable String month, HttpServletRequest request){
        request.getSession().setAttribute("month", month);
        return "sale/details";
    }
}
