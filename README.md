# Canteen

项目需求：

一个企业内的餐厅，需要实现内部网络自动订餐系统，系统基本功能如下：

1. 餐厅有个内部的食谱(recipe)，每周一前由餐厅经理从中选出几十种食品（例如菜肴，点心，主食，糖水），形成菜单(menu)，菜品需要有图片，价格，计量单位信息。每次生成的历史菜单需要留存。

2. 企业员工每天在上午9:00之前登录系统，从菜单中选择自己需要吃的菜品，生成一张订单(order form)

3. 9点以后，厨房主管可以打印出一份总括订单(blanket order)，包含每种菜肴(食品)的数量，用以控制加工菜肴的配料选取等工作。

4. 中午11:30，配餐员打印出所有的订单，用以配送午餐

5. 每月1号系统会自动进行统计工作，按照月度统计餐厅售出的菜品数量，总金额等信息，餐厅经理和财务管理也可以单独列出某位用户每月的所有订餐记录。

6. 内部食谱可以餐厅经理和厨房主管进行管理，删除或修改一个库中的菜式信息不能影响已有的点餐单，菜单或订餐记录。



启动说明：

- 需要在config -> WebMvcConfig中的 设置静态资源映射 将 file:C:/Users/30141/IdeaProjects/canteen/picture/改成当前项目的picture对应的本地路径
- 需要将controller -> CommonController中的文件上传接口中将 C:\Users\30141\IdeaProjects\canteen\picture\ 改成当前项目的picture对应的本地路径



项目结构：

```
-- picture      文件上传与下载的图片存放位置
-- sql          项目数据库文件
-- src          项目总代码
    -- main
        -- java
            -- com.javaweb.canteen 项目包结构
                -- common                       工具包
                -- config                       配置包
                -- controller                   控制器包
                -- entity                       实体包
                -- exception                    异常包
                -- interceptor                  拦截器包
                -- mapper                       dao包
                -- security                     安全包
                -- service                      服务包
                -- CanteenApplication.java      启动类
        -- resources
            -- static               静态资源
            -- templates            视图
            -- application.yml      springboot配置文件
            -- banner.txt           启动页面图标
-- pom.xml      maven依赖
```
