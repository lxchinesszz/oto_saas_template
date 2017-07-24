package phoenix.jhbank.config.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * Created by liuxin on 17/1/4.
 */
@Aspect
@Component
public class WebLogAspect {
    private final static Logger logger = LoggerFactory.getLogger(WebLogAspect.class);

    /**
     * rest包和子包里面的所有方法
     */
    @Pointcut("execution(public * phoenix.jhbank.rest..*.*(..))")
    public void weblog() {
    }

    @Before("weblog()")
    public void doBefore(JoinPoint joinpoint) throws Throwable {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        request.setAttribute("startTime", System.currentTimeMillis());
        // 记录下请求内容
        logger.info("----------" + joinpoint.getSignature().getName() + "方法开始执行----------------------------");
        logger.info("URL : " + request.getRequestURL().toString());
        logger.info("请求类型 : " + request.getMethod());
        logger.info("请求IP : " + request.getRemoteAddr());
        logger.info("方法 : " + joinpoint.getSignature().getDeclaringTypeName() + "." + joinpoint.getSignature().getName());
        logger.info("参数列表 : " + Arrays.toString(joinpoint.getArgs()));
    }

    @AfterReturning(returning = "ret", pointcut = "weblog()")
    public void doAfterReturning(Object ret) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Long startTime = (Long) request.getAttribute("startTime");
        Long endTime=System.currentTimeMillis();
        // 处理完请求，返回内容
        logger.info("返回参数 : " + ret);
        logger.info("-----------------方法执行完毕,耗时:{}ms-------------------",(endTime-startTime));
    }

}
