package com.javaweb.canteen.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.javaweb.canteen.common.R;
import com.javaweb.canteen.entity.MyUser;
import com.javaweb.canteen.exception.CustomException;
import com.javaweb.canteen.service.MyUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/user")
public class MyUserController {

    @Autowired
    private MyUserService myUserService;

    /**
     * 获取当前用户信息接口
     */
    @GetMapping("/getInfo")
    public R<MyUser> getInfo(HttpServletRequest request) {
        MyUser user = (MyUser) request.getSession().getAttribute("currUser");
        return R.success(user);
    }


    /**
     * 用户分页接口
     */
    @PreAuthorize("hasRole('manager')")
    @GetMapping("/page")
    public R<Page<MyUser>> getAllUsers(int page, int limit,
                                 @RequestParam(required = false) String name){

        Page<MyUser> pageInfo = new Page<>(page, limit);

        LambdaQueryWrapper<MyUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StrUtil.isNotEmpty(name), MyUser::getName, name)
                .orderByAsc(MyUser::getUserId);

        myUserService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 添加用户接口
     */
    @PreAuthorize("hasRole('manager')")
    @PostMapping("/add")
    public R<String> addUser(@RequestBody MyUser myUser){
        if (StrUtil.isEmpty(myUser.getUsername())) {
            throw new CustomException("用户名为空");
        }
        if (StrUtil.isEmpty(myUser.getName())) {
            throw new CustomException("昵称为空");
        }
        if (StrUtil.isEmpty(myUser.getTelephone())) {
            throw new CustomException("电话为空");
        }
        if (StrUtil.isEmpty(myUser.getDepartment())) {
            throw new CustomException("部门为空");
        }
        if (StrUtil.isEmpty(myUser.getRole())) {
            throw new CustomException("角色为空");
        }
        // 密码默认为123456
        myUser.setPassword("123456");
        boolean res = myUserService.save(myUser);
        if (res) {
            return R.success("添加成功");
        }else{
            return R.fail("添加失败");
        }
    }

    /**
     * 修改用户接口
     */
    @PreAuthorize("hasRole('manager')")
    @PutMapping("/update")
    public R<String> updateUser(@RequestBody MyUser myUser, HttpServletRequest request){
        if (StrUtil.isEmpty(myUser.getUsername())) {
            throw new CustomException("用户名为空");
        }
        if (StrUtil.isEmpty(myUser.getName())) {
            throw new CustomException("昵称为空");
        }
        if (StrUtil.isEmpty(myUser.getTelephone())) {
            throw new CustomException("电话为空");
        }
        if (StrUtil.isEmpty(myUser.getDepartment())) {
            throw new CustomException("部门为空");
        }
        if (StrUtil.isEmpty(myUser.getRole())) {
            throw new CustomException("角色为空");
        }
        MyUser user = (MyUser) request.getSession().getAttribute("editUser");
        myUser.setUserId(user.getUserId());
        boolean res = myUserService.updateById(myUser);
        if (res) {
            return R.success("修改成功");
        }else{
            return R.fail("修改失败");
        }
    }

    /**
     * 删除用户接口
     */
    @PreAuthorize("hasRole('manager')")
    @DeleteMapping("/delete/{userId}")
    public R<String> deleteUser(@PathVariable Long userId){
        boolean res = false;
        if (userId != null) {
            res = myUserService.removeById(userId);
        }
        if (res) {
            return R.success("删除成功");
        }else{
            return R.fail("删除失败");
        }
    }

    /**
     * 批量删除用户接口
     */
    @PreAuthorize("hasRole('manager')")
    @DeleteMapping("/deleteBatch/{userIds}")
    public R<String> deleteBatchUser(@PathVariable String userIds){
        boolean res = true;
        String[] ids = userIds.split(",");
        for (String id : ids) {
            Long userId = Long.valueOf(id);
            boolean flag = myUserService.removeById(userId);
            res = res && flag;
        }
        if (res) {
            return R.success("批量删除成功");
        }else{
            return R.fail("批量删除失败");
        }
    }

    /**
     * 修改用户密码接口
     */
    @PostMapping("/updatePwd")
    public R<String> updatePsw(@RequestParam String password, HttpServletRequest request){
        MyUser user = (MyUser) request.getSession().getAttribute("currUser");
        String username = user.getUsername();
        LambdaUpdateWrapper<MyUser> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(MyUser::getUsername, username).set(MyUser::getPassword, password);
        boolean res = myUserService.update(null, updateWrapper);
        if (res) {
            user.setPassword(password);
            request.getSession().setAttribute("currUser", user);
            return R.success("更新成功");
        }else{
            return R.fail("更新失败");
        }
    }
}
