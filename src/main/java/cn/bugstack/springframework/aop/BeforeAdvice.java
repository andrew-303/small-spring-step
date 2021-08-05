package cn.bugstack.springframework.aop;

import org.aopalliance.aop.Advice;

/**
 * Common marker interface for before advice, such as {@link MethodBeforeAdvice}.
 * 前通知的通用标记接口，例如 {@link MethodBeforeAdvice}
 */
public interface BeforeAdvice  extends Advice {
}
