package org.example.common.config;

import jakarta.annotation.Resource;
import org.example.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtIntercepter implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(JwtIntercepter.class);  //创建了一个日志记录器
    @Resource     // Spring 自动帮你 new 对象并赋值
    private AdminService adminService;    //// 这是 Bean

}
