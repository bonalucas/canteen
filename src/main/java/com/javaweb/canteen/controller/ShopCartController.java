package com.javaweb.canteen.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.javaweb.canteen.common.R;
import com.javaweb.canteen.entity.MyUser;
import com.javaweb.canteen.entity.ShopCart;
import com.javaweb.canteen.exception.CustomException;
import com.javaweb.canteen.service.ShopCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shopCart")
public class ShopCartController {
    @Autowired
    private ShopCartService shopCartService;

    /**
     * 购物车添加接口
     */
    @PostMapping("/add")
    public R<String> saveInfo(@RequestBody ShopCart shopCart, HttpServletRequest request){
        if (shopCart == null) {
            throw new CustomException("购物车基本信息为空,无法加入购物车");
        }
        boolean res;
        String name = shopCart.getName();
        LambdaQueryWrapper<ShopCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShopCart::getName, name);
        ShopCart info = shopCartService.getOne(queryWrapper);
        if (info != null) {
            Integer weight = shopCart.getWeight() + info.getWeight();
            double total = weight * info.getPrice();
            LambdaUpdateWrapper<ShopCart> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(ShopCart::getName, name)
                    .set(ShopCart::getWeight, weight)
                    .set(ShopCart::getTotalPrice, total);
            res = shopCartService.update(null, updateWrapper);
        }else{
            MyUser user = (MyUser) request.getSession().getAttribute("currUser");
            shopCart.setUserId(user.getUserId());
            shopCart.setTotalPrice(shopCart.getPrice() * shopCart.getWeight());
            res = shopCartService.save(shopCart);
        }
        if (res) {
            return R.success("加入成功");
        }else{
            return R.fail("加入失败");
        }
    }

    /**
     * 购物车信息获取接口
     */
    @GetMapping("/get")
    public R<List<ShopCart>> getInfo(HttpServletRequest request){
        MyUser currUser = (MyUser) request.getSession().getAttribute("currUser");
        Long userId = currUser.getUserId();
        LambdaQueryWrapper<ShopCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShopCart::getUserId, userId);
        List<ShopCart> list = shopCartService.list(queryWrapper);
        if (list != null) {
            return R.success(list);
        }else {
            return R.fail("获取购物车信息失败");
        }
    }

    /**
     * 购物车删除接口
     */
    @DeleteMapping("/delete/{scId}")
    public R<String> delete(@PathVariable Long scId){
        if (scId == null)  return R.fail("无法获取购物车编号");
        boolean res = shopCartService.removeById(scId);
        if (res) {
            return R.success("删除成功");
        }else{
            return R.fail("删除失败");
        }
    }
    
}
