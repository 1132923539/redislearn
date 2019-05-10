package cn.luxinhuo.redislearn.aop;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented  //③：注解是否将包含在 JavaDoc 中
public @interface AopLog {

    /**
     * 日志描述信息
     * @return
     */
    @AliasFor("value")
    String description() default "";
}
