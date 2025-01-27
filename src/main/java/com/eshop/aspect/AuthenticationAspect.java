package com.eshop.aspect;

import com.eshop.annotation.AccessControl;
import com.eshop.dto.UserDto;
import com.eshop.entity.Constant;
import com.eshop.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

@Aspect
@Component
public class AuthenticationAspect {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationAspect.class);

    @Pointcut("@annotation(com.eshop.annotation.AccessControl)")
    private void requestInterceptor(){}

    @Before("requestInterceptor()")
    public void interceptorAction(JoinPoint point) throws BusinessException {
        try {
            Object target = point.getTarget();
            Object[] arguments = point.getArgs();
            String methodName = point.getSignature().getName();
            Class<?>[] parameterTypes = ((MethodSignature) point.getSignature()).getMethod().getParameterTypes();
            Method method = target.getClass().getMethod(methodName, parameterTypes);
            AccessControl interceptor = method.getAnnotation(AccessControl.class);
            if (null == interceptor) {
                return;
            }
            if (interceptor.requireLogin() || interceptor.requireAdmin()) {
                checkLogin(interceptor.requireAdmin());
            }
        } catch (Exception e) {
            logger.error("Access Control error", e);
            throw new BusinessException("Access error");
        }
    }

    private void checkLogin(Boolean isAdmin) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        UserDto userDto = (UserDto) session.getAttribute(Constant.SESSION_KEY);

        if(userDto == null) {
            try{
                throw new BusinessException("Session timeout. Please login again.");
            } catch (BusinessException e) {
                throw new RuntimeException(e);
            }
        }

        if(isAdmin && !userDto.getIsAdmin()) {
            try {
                throw new BusinessException("No Permission");
            } catch (BusinessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
