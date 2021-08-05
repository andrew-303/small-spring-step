package cn.bugstack.springframework.test;

import cn.bugstack.springframework.beans.PropertyValue;
import cn.bugstack.springframework.beans.PropertyValues;
import cn.bugstack.springframework.beans.factory.config.BeanDefinition;
import cn.bugstack.springframework.beans.factory.config.BeanReference;
import cn.bugstack.springframework.beans.factory.support.DefaultListableBeanFactory;
import cn.bugstack.springframework.test.bean.back.UserDao;
import cn.bugstack.springframework.test.bean.UserService;
import cn.bugstack.springframework.test.bean.back.UserServiceInfo;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 博客：https://bugstack.cn - 沉淀、分享、成长，让自己和他人都能有所收获！
 * 公众号：bugstack虫洞栈
 * Create by 小傅哥(fustack)
 */
public class ApiTest {

    @Test
    public void test_BeanFactory(){
        // 1.初始化 BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // 2.注册 bean
        BeanDefinition beanDefinition = new BeanDefinition(UserService.class);
        beanFactory.registerBeanDefinition("userService", beanDefinition);

        // 3.第一次获取 bean
        UserService userService = (UserService) beanFactory.getBean("userService");
        userService.queryUserInfo();
//        System.out.println(userService);

//        System.out.println("第二次获取 bean from Singleton");

        // 4.第二次获取 bean from Singleton
        UserService userService_singleton = (UserService) beanFactory.getBean("userService");
        userService_singleton.queryUserInfo();
        System.out.println(userService_singleton);
    }

    @Test
    public void test_BeanFactory2() {
        // 1.初始化Bean工厂
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // 2.注入Bean
        BeanDefinition beanDefinition = new BeanDefinition(UserService.class);
        beanFactory.registerBeanDefinition("userService",beanDefinition);
        // 3.获取Bean
        UserService userService = (UserService) beanFactory.getBean("userService", "dingding");
//        UserService userService = (UserService) beanFactory.getBean("userService");

        userService.queryUserInfo();
    }

    @Test
    public void test_newInstance() throws IllegalAccessException, InstantiationException {
        UserService userService = UserService.class.newInstance();//无参构造函数
        System.out.println(userService.getClass().getName());
    }

    @Test
    public void test_constructor() throws Exception {
        Class<UserService> userServiceClass = UserService.class;
        Constructor<UserService> userServiceConstructor = userServiceClass.getDeclaredConstructor(String.class);
        UserService userService = userServiceConstructor.newInstance("hello");
        System.out.println(userService.getClass());
    }

    @Test
    public void test_parameterTypes() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<UserService> beanClass = UserService.class;
        Constructor<?>[] constructors = beanClass.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            System.out.println(constructor.getParameterTypes().length);
        }
        Constructor<?> constructor = constructors[1];
        Constructor<UserService> userServiceConstructor = beanClass.getDeclaredConstructor(constructor.getParameterTypes());
        UserService userService = userServiceConstructor.newInstance("hello");
        System.out.println(userService);

    }

    @Test
    public void test_cglib() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(UserService.class);
        enhancer.setCallback(new NoOp() {
            @Override
            public int hashCode() {
                return super.hashCode();
            }
        });
        Object obj = enhancer.create(new Class[]{String.class}, new Object[]{"hello"});
        System.out.println(obj);
    }

    @Test
    public void test_BeanFactory3() {
        // 1.初始化 BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // 2. UserDao 注册
        beanFactory.registerBeanDefinition("userDao",new BeanDefinition(UserDao.class));

        // 3. UserService 设置属性[uId、userDao]
        PropertyValues propertyValues = new PropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("uId","10002"));
        propertyValues.addPropertyValue(new PropertyValue("userDao",new BeanReference("userDao")));

        // 4. UserService 注入bean
        BeanDefinition beanDefinition = new BeanDefinition(UserServiceInfo.class, propertyValues);
        beanFactory.registerBeanDefinition("userServiceInfo",beanDefinition);

        // 5. UserService 获取bean
        UserServiceInfo userServiceInfo = (UserServiceInfo) beanFactory.getBean("userServiceInfo");
        userServiceInfo.queryUserInfo();
    }

}
