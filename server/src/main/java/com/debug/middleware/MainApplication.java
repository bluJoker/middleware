package com.debug.middleware;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 *     这样就能生成WAR文件了。但如果WAR文件里没有启用Spring MVC DispatcherServlet
 * 的web.xml文件或者Servlet初始化类，这个WAR文件就一无是处。
 *     此时就该Spring Boot出马了。它提供的SpringBootServletInitializer是一个支持
 * Spring Boot的Spring WebApplicationInitializer实现。除了配置Spring的Dispatcher-
 * Servlet，SpringBootServletInitializer还会在Spring应用程序上下文里查找Filter、
 * Servlet或ServletContextInitializer类型的Bean，把它们绑定到Servlet容器里。
 *     要使用SpringBootServletInitializer，只需创建一个子类，覆盖configure()方法
 * 来指定Spring配置类。
 *
 *     此时，这个类既是启动类（带有main()方法），也是一个Spring配置类。
 *
 * -----SpringBoot实战p141
 * */

@SpringBootApplication
@MapperScan(basePackages = "com.debug.middleware.model")
public class MainApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return super.configure(builder);
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}
