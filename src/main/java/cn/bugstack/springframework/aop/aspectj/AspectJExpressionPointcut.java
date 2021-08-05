package cn.bugstack.springframework.aop.aspectj;

import cn.bugstack.springframework.aop.ClassFilter;
import cn.bugstack.springframework.aop.MethodMatcher;
import cn.bugstack.springframework.aop.Pointcut;
import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.PointcutPrimitive;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class AspectJExpressionPointcut implements Pointcut, ClassFilter, MethodMatcher {

    /**
     * PointcutPrimitive 表示AspectJ 支持的不同类型切入点原语的枚举
     */
    private static final Set<PointcutPrimitive> SUPPORTED_PRIMITIVES = new HashSet<PointcutPrimitive>();

    static {
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.EXECUTION);
    }

    private final PointcutExpression pointcutExpression;

    public AspectJExpressionPointcut(String expression) {
        // 返回一个切入点解析器，它可以解析从用户定义的 AspectJ 支持的切入点原语子集构建的切入点表达式
        PointcutParser pointcutParser = PointcutParser.getPointcutParserSupportingSpecifiedPrimitivesAndUsingSpecifiedClassLoaderForResolution(SUPPORTED_PRIMITIVES, this.getClass().getClassLoader());
        // 解析给定的切入点表达式。假定全局范围用于解析任何类型引用，并且切入点必须不包含形式（要绑定的变量）
        pointcutExpression = pointcutParser.parsePointcutExpression(expression);
    }

    @Override
    public boolean matches(Class<?> clazz) {
        // 确定此切入点是否可以匹配给定类中的连接点。
        return pointcutExpression.couldMatchJoinPointsInType(clazz);
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        // 确定此切入点是否与给定方法的执行相匹配
        return pointcutExpression.matchesMethodExecution(method).alwaysMatches();
    }

    @Override
    public ClassFilter getClassFilter() {
        return this;
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return this;
    }


}
