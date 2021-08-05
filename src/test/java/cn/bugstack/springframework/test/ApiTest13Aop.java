package cn.bugstack.springframework.test;

import cn.bugstack.springframework.context.support.ClassPathXmlApplicationContext;
import cn.bugstack.springframework.test.bean.IUserService;
import cn.bugstack.springframework.test.bean.IUserServiceProxy;
import org.junit.Test;

public class ApiTest13Aop {

    @Test
    public void test_aop() {
//        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:springAop.xml");
        IUserService userService = applicationContext.getBean("userService", IUserService.class);
        System.out.println("测试结果："  + userService.queryUserInfo());

    }
}
