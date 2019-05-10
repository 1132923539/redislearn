package cn.luxinhuo.redislearn.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;

@Slf4j
@Aspect
@Component
@Profile({"!prod"})
public class AopLogAspect {
    // 换行符
    private static final String LINE_SEPARATOR = System.lineSeparator();

    /**
     * 以自定义的注解为切点，则使用了此注解的地方都将能够执行这个切面的操作
     */
    @Pointcut("@annotation(cn.luxinhuo.redislearn.aop.AopLog)")
    public void aopLog(){}

    /**
     * 在切点之前织入
     */
    @Before("aopLog()")
    public void doBefore(JoinPoint joinPoint) throws ClassNotFoundException {
        // 获取httprequest
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();

        // 获取注解中所定义的描述信息
        String aopLogDesc = getAspectLogDescription(joinPoint);

        //打印请求相关参数
        log.info("=========================================== start ==========================================");
        log.info("URL              : {}",request.getRequestURL().toString());
        log.info("Description      : {}",aopLogDesc);
        log.info("Http Method      : {}",request.getMethod());
        log.info("Method Signature : {}",joinPoint.getSignature());
        log.info("Request IP       : {}",request.getRemoteAddr());
        log.info("Request Args     : {}",new Gson().toJson(joinPoint.getArgs()));

    }

    @After("aopLog()")
    public void doAfter(){
        log.info("=========================================== end ==========================================");
    }

    @Around("aopLog()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Instant start = Instant.now();
        Object result = proceedingJoinPoint.proceed();
        log.info("Response Args    : {}",new Gson().toJson(result));
        log.info("Time-Consuming   : {} ms", Duration.between(start,Instant.now()).toMillis());
        return result;
    }

    /**
     * 获取切面注解中的描述
     */
    private String getAspectLogDescription(JoinPoint joinPoint) throws ClassNotFoundException {
        String targetClassName = joinPoint.getTarget().getClass().getName();
        String targetMethodName = joinPoint.getSignature().toLongString();
        Class<?> targetClass = Class.forName(targetClassName);
        Method[] methods = targetClass.getMethods();

        StringBuilder sb = new StringBuilder();
        for (Method method : methods) {
            // 通过切点方法签名找到方法
            if (method.toString().equals(targetMethodName)){
                sb.append(method.getAnnotation(AopLog.class).description());
                break;
            }
        }
        return sb.toString();
    }
}
