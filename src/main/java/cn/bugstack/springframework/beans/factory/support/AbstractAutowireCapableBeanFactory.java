package cn.bugstack.springframework.beans.factory.support;

import cn.bugstack.springframework.beans.BeansException;
import cn.bugstack.springframework.beans.PropertyValue;
import cn.bugstack.springframework.beans.PropertyValues;
import cn.bugstack.springframework.beans.factory.*;
import cn.bugstack.springframework.beans.factory.config.*;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * 实例化Bean类
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {

    private InstantiationStrategy instantiationStrategy = new CglibSubclassingInstantiationStrategy();
    private InstantiationStrategy simInstantiationStrategy = new SimpleInstantiationStrategy();

    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition,Object[] args) throws BeansException {
//        System.out.println("5.AbstractAutowireCapableBeanFactory.createBean: ");
        System.out.println("beanName: " + beanName + ",BeanDefinition: " + beanDefinition.getClass().getName());
        Object bean = null;
        try {
            // 判断是否返回代理Bean对象
            bean =resolveBeforeInstantiation(beanName, beanDefinition);
            if (null != bean) {
                return bean;
            }

            // 实例化bean
            bean = createBeanInstance(beanDefinition, beanName, args);
            System.out.println("bean: " + bean.getClass().getName());
            //给bean填充属性
            applyPropertyValues(beanName, bean,beanDefinition);
            // 执行 Bean 的初始化方法和 BeanPostProcessor 的前置和后置处理方法
            bean = initializeBean(beanName, bean, beanDefinition);
            System.out.println("initializeBean: " + bean.getClass().getName());
        } catch (Exception e) {
            throw new BeansException("Instantiation of bean failed", e);
        }

        // 注册实现了 DisposableBean 接口的 Bean 对象
        registerDisposableBeanIfNecessary(beanName, bean, beanDefinition);

        // 判断 SCOPE_SINGLETON、SCOPE_PROTOTYPE 单例模式和原型模式的区别就在于是否存放到内存中，如果是原型模式那么就不会存放到内存中，每次获取都重新创建对象
        if (beanDefinition.isSingleton()) {
//            addSingleton(beanName,bean);
            registerSingleton(beanName,bean);
        }
        return bean;
    }

    protected Object resolveBeforeInstantiation(String beanName, BeanDefinition beanDefinition) {
        Object bean = applyBeanPostProcessorsBeforeInstantiation(beanDefinition.getBeanClass(), beanName);
        if (null != bean) {
            bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
        }
        return bean;
    }

    protected Object applyBeanPostProcessorsBeforeInstantiation(Class<?> beanClass, String beanName) {
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
                Object result = ((InstantiationAwareBeanPostProcessor)beanPostProcessor).postProcessBeforeInitialization(beanClass, beanName);
                if(null != result) return result;
            }
        }
        return null;
    }

    protected void registerDisposableBeanIfNecessary(String beanName, Object bean, BeanDefinition beanDefinition) {
        System.out.println("registerDisposableBeanIfNecessary,beanName:" + beanName  + ", " + bean.getClass().getSimpleName() + ", "+beanDefinition.getClass().getSimpleName() );
        // 非单例类型的Bean不执行小会
        if(!beanDefinition.isSingleton()) return;

        if (bean instanceof DisposableBean || StrUtil.isNotEmpty(beanDefinition.getDestroyMethodName())) {
            System.out.println("bean instanceof DisposableBean || StrUtil.isNotEmpty(beanDefinition.getDestroyMethodName())");
            registerDisposableBean(beanName, new DisposableBeanAdapter(bean, beanName, beanDefinition));
        }
    }

    protected Object createBeanInstance(BeanDefinition beanDefinition, String beanName, Object[] args) {
//        System.out.println("AbstractAutowireCapableBeanFactory.createBeanInstance: ");
        Constructor constructorToUse =  null;
        Class<?> beanClass = beanDefinition.getBeanClass();
        Constructor<?>[] declaredConstructors = beanClass.getDeclaredConstructors();
        /*if (args == null) {
            constructorToUse = declaredConstructors[0];
            return simInstantiationStrategy.instantiate(beanDefinition,beanName,constructorToUse,null);
        } else {*/
            for (Constructor ctor : declaredConstructors) {
                if (null != args && ctor.getParameterTypes().length == args.length) {
//                    System.out.println(args.length);
                    constructorToUse = ctor;
                    break;
                }
            }

            return getInstantiationStrategy().instantiate(beanDefinition,beanName,constructorToUse,args);
        //}

    }

    /**
     * Bean属性填充
     */
    protected void applyPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {
        System.out.println("called applyPropertyValues");
        System.out.println("beanName: " + beanName);
        System.out.println("bean: " + bean.getClass().getName());
        try {
            PropertyValues propertyValues = beanDefinition.getPropertyValues();
            for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {
                String name = propertyValue.getName();
                Object value = propertyValue.getValue();
                System.out.println("PropertyValue,name: " + name + ",value: " + value);
                if (value instanceof BeanReference) {
                    // A依赖B  获取B的实例化
                    BeanReference beanReference = (BeanReference) value;
                    value = getBean(beanReference.getBeanName());
                    System.out.println("value instanceof BeanReference,value: " + value);
                }
                //属性填充
                BeanUtil.setFieldValue(bean,name,value);
            }
        } catch (Exception e) {
            throw new BeansException("Error setting property values：" + beanName);
        }
    }

    public InstantiationStrategy getInstantiationStrategy() {
        return instantiationStrategy;
    }

    public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
        this.instantiationStrategy = instantiationStrategy;
    }

    private Object initializeBean(String beanName, Object bean, BeanDefinition beanDefinition) {
        System.out.println("called initializeBean");
        System.out.println("beanName: " + beanName);
        System.out.println("bean: " + bean.getClass().getName());
        // invokeAwareMethods
        if (bean instanceof Aware) {
            System.out.println("bean instanceof Aware");
            if (bean instanceof BeanFactoryAware) {
                System.out.println("bean instanceof BeanFactoryAware");
                ((BeanFactoryAware) bean).setBeanFactory(this);
            }
            if (bean instanceof BeanClassLoaderAware){
                System.out.println("bean instanceof BeanClassLoaderAware");
                ((BeanClassLoaderAware) bean).setBeanClassLoader(getBeanClassLoader());
            }
            if (bean instanceof BeanNameAware) {
                ((BeanNameAware) bean).setBeanName(beanName);
            }
        }
        //1.执行BeanPostProcessor Before处理
        Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);
        System.out.println("wrappedBean: " + wrappedBean.getClass().getName());

        // 待完成内容：invokeInitMethods(beanName, wrappedBean, beanDefinition);
        try {
            invokeInitMethods(beanName, wrappedBean, beanDefinition);
        } catch (Exception e) {
            throw new BeansException("Invocation of init method of bean[" + beanName + "] failed", e);
        }

        // 2. 执行 BeanPostProcessor After 处理
        wrappedBean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
        System.out.println("wrappedBean post: " + wrappedBean.getClass().getName());
        return wrappedBean;
    }

    private void invokeInitMethods(String beanName, Object bean, BeanDefinition beanDefinition) throws Exception {
        System.out.println("called invokeInitMethods");
        // 1. 实现接口 InitializingBean
        if (bean instanceof InitializingBean) {
            System.out.println("bean instanceof InitializingBean");
            ((InitializingBean)bean).afterPropertiesSet();
            System.out.println("bean instanceof InitializingBean post");
        }

        // 2. 注解配置 init-method {判断是为了避免二次执行销毁}
        String initMethodName = beanDefinition.getInitMethodName();
        System.out.println("initMethodName: " + initMethodName);
        if (StrUtil.isNotEmpty(initMethodName)) {
            Method initMethod = beanDefinition.getBeanClass().getMethod(initMethodName);
            if (null == initMethod) {
                throw new BeansException("Could not find an init method named '" + initMethodName + "' on bean with name '" + beanName + "'");
            }
            initMethod.invoke(bean);
        }

    }

    @Override
    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            Object current = processor.postProcessBeforeInitialization(result, beanName);
            if(null == current) return result;
            result = current;
        }
        return result;
    }

    @Override
    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            Object current = processor.postProcessAfterInitialization(result, beanName);
            if(null == current) return result;
            result = current;
        }
        return result;
    }

}
