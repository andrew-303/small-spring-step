package cn.bugstack.springframework.beans.factory;

/**
 * 实现此接口，既能感知所属的BeanName
 */
public interface BeanNameAware extends Aware{

    void setBeanName(String name);

}
