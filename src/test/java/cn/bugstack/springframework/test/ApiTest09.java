package cn.bugstack.springframework.test;

import cn.bugstack.springframework.context.support.ClassPathXmlApplicationContext;
import cn.bugstack.springframework.test.bean.back.UserServiceInfo;
import org.junit.Test;

public class ApiTest09 {

    @Test
    public void test_xml() {
        // 1.初始化bean工厂
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        applicationContext.registerShutdownHook();

        // 2.获取Bean对象调用方法
        UserServiceInfo userServiceInfo = applicationContext.getBean("userServiceInfo", UserServiceInfo.class);
        String result = userServiceInfo.queryUserInfo();
        System.out.println("结果：" + result);

        System.out.println("ApplicationContextAware: " + userServiceInfo.getApplicationContext());
        System.out.println("BeanFactoryAware: " + userServiceInfo.getBeanFactory());
    }
}
