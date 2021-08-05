package cn.bugstack.springframework.beans.factory;

import cn.bugstack.springframework.beans.BeansException;

/**
 * Bean工厂，获取Bean
 */
public interface BeanFactory {

    Object getBean(String name) throws BeansException;

    // 重载一个含有入参信息的args的getBean方法，这样就可以方便的传递入参给构造函数实例化了
    Object getBean(String name, Object... args) throws BeansException;

    <T> T getBean(String name,Class<T> requiredType) throws BeansException;

}
