package cn.bugstack.springframework.test;

import cn.bugstack.springframework.context.support.ClassPathXmlApplicationContext;
import cn.bugstack.springframework.test.event.CustomEvent;
import org.junit.Test;

public class ApiTest11EventListener {

    @Test
    public void test_xml() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:springListenerEvent.xml");
        applicationContext.publishEvent(new CustomEvent(applicationContext,1019129009086763L,"事件监听成功"));

        applicationContext.registerShutdownHook();
    }
}
