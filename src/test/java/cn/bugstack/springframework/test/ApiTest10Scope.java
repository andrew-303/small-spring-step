package cn.bugstack.springframework.test;

import cn.bugstack.springframework.context.support.ClassPathXmlApplicationContext;
import cn.bugstack.springframework.test.bean.back.UserServiceInfo;
import org.junit.Test;
import org.openjdk.jol.info.ClassLayout;

/**
 * 在 spring.xml 配置文件中，设置了 scope=”prototype” 这样就每次获取到的对象都应该是一个新的对象。
 * 这里判断对象是否为一个会看到打印的类对象的哈希值，所以我们把十六进制哈希打印出来。
 */
public class ApiTest10Scope {

    @Test
    public void test_scope() {
        // 1.初始化 BeanFactory
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:springProxy.xml");
        applicationContext.registerShutdownHook();

        // 2. 获取Bean对象调用方法
        UserServiceInfo userServiceInfo01 = applicationContext.getBean("userServiceInfo", UserServiceInfo.class);
        UserServiceInfo userServiceInfo02 = applicationContext.getBean("userServiceInfo", UserServiceInfo.class);

        // 3. 配置 scope="prototype/singleton"
        System.out.println(userServiceInfo01);
        System.out.println(userServiceInfo02);

        // 4. 打印十六进制哈希
        System.out.println(userServiceInfo01 + "十六进制哈希: " + Integer.toHexString(userServiceInfo01.hashCode()));
        System.out.println(ClassLayout.parseInstance(userServiceInfo01).toPrintable());

    }

    @Test
    public void test_factory_bean() {
        // 1.初始化 BeanFactory
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:springProxy.xml");
        applicationContext.registerShutdownHook();

        // 2. 调用代理方法
        UserServiceInfo userServiceInfo = applicationContext.getBean("userServiceInfo", UserServiceInfo.class);
        System.out.println("测试结果" + userServiceInfo.queryUserInfo());

    }


}
