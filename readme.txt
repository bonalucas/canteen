启动说明：
    需要在config -> WebMvcConfig中的 设置静态资源映射 将 file:C:/Users/30141/IdeaProjects/canteen/picture/改成当前项目的picture对应的本地路径
    需要将controller -> CommonController中的文件上传接口中将 C:\Users\30141\IdeaProjects\canteen\picture\ 改成当前项目的picture对应的本地路径

项目结构说明：
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
