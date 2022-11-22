package com.javaweb.canteen.config;

import com.javaweb.canteen.common.JacksonObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
    /**
     *  配置视图控制
     */
    @Override
    protected void addViewControllers(ViewControllerRegistry registry) {
        // 登录页面跳转
        registry.addViewController("/login").setViewName("login");
        // 错误页面跳转
        registry.addViewController("/to403").setViewName("error/403");
        registry.addViewController("/to404").setViewName("error/404");
        registry.addViewController("/to500").setViewName("error/500");
        // 后台首页框架页面跳转
        registry.addViewController("/back/toIndex").setViewName("index");
        // 个人信息页面跳转
        registry.addViewController("/back/toPerson").setViewName("user/person");
        // 用户管理添加页面跳转
        registry.addViewController("/back/toUserAdd").setViewName("user/add");
        // 食谱管理添加页面跳转
        registry.addViewController("/back/toRecipeAdd").setViewName("recipe/add");
        // 个人信息修改密码页面跳转
        registry.addViewController("/back/toUpdatePwd").setViewName("user/pwd");
    }

    /**
     * 设置静态资源映射
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 相当于当网址访问到localhost:8080/会映射到对应的resource目录下
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/picture/**")
                .addResourceLocations("file:C:/Users/30141/IdeaProjects/canteen/picture/");
    }

    /**
     * 扩展mvc框架的消息转换器
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 创建消息转换器对象
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        // 设置对象转换器，底层使用Jackson将Java对象转换为JSON
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        // 将上面的转换器加入mvc框架的转换器集合中
        converters.add(0, messageConverter);
    }

    /**
     * 配置拦截器
     */
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new StaffOrderInterceptor()).addPathPatterns("/order/submit");
    }
}
