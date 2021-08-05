package cn.bugstack.springframework.beans.factory;
/**
 * Interface to be implemented by objects used within a {@link BeanFactory}
 * which are themselves factories. If a bean implements this interface,
 * it is used as a factory for an object to expose, not directly as a bean
 * instance that will be exposed itself.
 * 由 {@link BeanFactory}中使用的对象实现的接口，这些对象本身就是工厂。如果 bean 实现了这个接口，
 * 它被用作对象公开的工厂，而不是直接作为将自身公开的 bean 实例。
 * @param <T>
 *
 */
public interface FactoryBean<T> {

    T getObject() throws Exception;

    Class<?> getObjectType();

    boolean isSingleton();

}
