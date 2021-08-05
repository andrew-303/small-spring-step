package cn.bugstack.springframework.test;

import cn.bugstack.springframework.context.support.ClassPathXmlApplicationContext;
import cn.bugstack.springframework.test.bean.back.UserServiceInfo;
import org.junit.Test;

public class ApiTest08 {

    @Test
    public void test_xml() {
        // 1.注册Bean Factory
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        applicationContext.registerShutdownHook();

        // 2.获取Bean对象调用方法
        UserServiceInfo userServiceInfo = applicationContext.getBean("userServiceInfo", UserServiceInfo.class);
        String result = userServiceInfo.queryUserInfo();
        System.out.println("测试结果：" + result);

    }


}
