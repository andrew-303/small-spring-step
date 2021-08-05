package cn.bugstack.springframework.beans.factory.support;

import cn.bugstack.springframework.beans.BeansException;
import cn.bugstack.springframework.beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class SimpleInstantiationStrategy implements InstantiationStrategy{

    @Override
    public Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor ctor, Object[] args) throws BeansException {
//        System.out.println("SimpleInstantiationStrategy.instantiate");
        // 获取Class信息，这个类信息是Bean定义的时候传递进去的。
        Class clazz = beanDefinition.getBeanClass();
        try {
            if (null != ctor) { //有参构造器
                return clazz.getDeclaredConstructor(ctor.getParameterTypes()).newInstance(args);
            } else {    // 无参构造器
                return clazz.getDeclaredConstructor().newInstance();
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new BeansException("Failed to instantiate [" + clazz.getName() + "]", e);
        }
    }
}
