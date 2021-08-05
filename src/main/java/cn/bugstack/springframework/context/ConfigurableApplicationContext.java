package cn.bugstack.springframework.context;

import cn.bugstack.springframework.beans.BeansException;

public interface ConfigurableApplicationContext extends ApplicationContext{

    /**
     *  刷新容器
     */
    void refresh() throws BeansException;

    /**
     * 注册虚拟机钩子
     */
    void registerShutdownHook();

    /**
     * 手动关闭
     */
    void close();
}
