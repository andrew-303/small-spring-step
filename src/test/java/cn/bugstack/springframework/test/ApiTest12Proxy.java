package cn.bugstack.springframework.test;

import cn.bugstack.springframework.aop.aspectj.AspectJExpressionPointcut;
import cn.bugstack.springframework.test.bean.*;
import cn.bugstack.springframework.test.bean.back.UserServiceInfo;
import org.junit.Test;

import java.lang.reflect.Method;

public class ApiTest12Proxy {

    @Test
    public void test_aop() throws NoSuchMethodException {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut("execution(* cn.bugstack.springframework.test.bean.UserServiceProxy.*(..))");
        Class<UserService> userServiceProxyClass = UserService.class;
//        Class<UserServiceProxy> userServiceProxyClass = UserServiceProxy.class;

        Method method = userServiceProxyClass.getDeclaredMethod("queryUserInfo");
        System.out.println("pointcut.matches: "+ pointcut.matches(userServiceProxyClass));
        System.out.println("pointcut.matches(method, clazz): " + pointcut.matches(method,userServiceProxyClass));
    }


    @Test
    public void test_proxy_method() {

        // 目标对象(可以替换成任何的目标对象)
        Object targetObject = new UserServiceInfo();

        //AOP代理
        /*Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), targetObject.getClass().getInterfaces(), new InvocationHandler(){
            //方法匹配器

        });*/

    }

    /*@Test
    public void test_dynamic() {
        //目标对象
        IUserServiceProxy userServiceProxy = new UserServiceProxy();

        // 组装代理对象
        AdvisedSupport advisedSupport = new AdvisedSupport();
        advisedSupport.setTargetSource(new TargetSource(userServiceProxy));
        advisedSupport.setMethodInterceptor(new UserServiceInterceptor());
        advisedSupport.setMethodMatcher(new AspectJExpressionPointcut("execution(* cn.bugstack.springframework.test.bean.IUserServiceProxy.*(..))"));

        // 代理对象(JdkDynamicAopProxy)
        IUserServiceProxy proxy_jdk = (IUserServiceProxy) new JdkDynamicAopProxy(advisedSupport).getProxy();
        //测试调用
        System.out.println("测试结果：" +         proxy_jdk.queryUserInfo());

        IUserServiceProxy proxy_cglib = (IUserServiceProxy) new Cglib2AopProxy(advisedSupport).getProxy();
        System.out.println("测试结果：" + proxy_cglib.register("哈哈"));
    }*/
}
