package cn.bugstack.springframework.beans.factory.support;

import cn.bugstack.springframework.beans.BeansException;
import cn.bugstack.springframework.beans.factory.FactoryBean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Support base class for singleton registries which need to handle
 * {@link cn.bugstack.springframework.beans.factory.FactoryBean} instances,
 * integrated with {@link DefaultSingletonBeanRegistry}'s singleton management.
 * FactoryBean注册服务
 */
public abstract class FactoryBeanRegistrySupport extends DefaultSingletonBeanRegistry{

    /**
     * Cache of singleton objects created by FactoryBeans: FactoryBean name --> object
     * FactoryBeans 创建的单例对象的缓存：FactoryBean 名称 --> 对象
     */
    private final Map<String,Object> factoryBeanObjectCache = new ConcurrentHashMap<String,Object>();

    /**
     * 为FactoryBean获取缓存对象
     */
    protected Object getCachedObjectForFactoryBean(String beanName) {
        Object object = factoryBeanObjectCache.get(beanName);
        return (object != NULL_OBJECT ? object : null);
    }

    /**
     * 单例情况，先从缓存获取对象，如果没有则从FactoryBean直接获取并且放入缓存
     * 非单例情况，直接从FactoryBean获取对象
     */
    protected Object getObjectFromFactoryBean(FactoryBean factoryBean, String beanName) {
        if (factoryBean.isSingleton()) {
            Object object = this.factoryBeanObjectCache.get(beanName);
            if (object == null) {
                 object = doGetObjectFromFactoryBean(factoryBean, beanName);
                 this.factoryBeanObjectCache.put(beanName,(object!=null ? object : NULL_OBJECT));
            }
            return (object != NULL_OBJECT ? object : null);
        } else {
            return doGetObjectFromFactoryBean(factoryBean, beanName);
        }
    }

    /**
     * 从FactoryBean中获取对象
     */
    private Object doGetObjectFromFactoryBean(final FactoryBean factoryBean, final String beanName) {
        try {
            return factoryBean.getObject();
        } catch (Exception e) {
            throw new BeansException("FactoryBean threw exception on object[" + beanName + "] creation",e);
        }
    }


}
