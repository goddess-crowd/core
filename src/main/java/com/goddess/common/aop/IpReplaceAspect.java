package com.goddess.common.aop;

import com.alibaba.boot.nacos.discovery.properties.Register;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.client.naming.utils.NetUtils;
import com.goddess.common.util.RealIPUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * @author 失败女神
 * @email: 18733123202@163.com
 * @date 2021/4/16 下午2:08
 * @Copyright © 女神帮
 */
@Slf4j
@Component
@Aspect
public class IpReplaceAspect {

    @Pointcut("execution(public * com.alibaba.boot.nacos.discovery.properties.NacosDiscoveryProperties.getRegister(..))")
    public void replace() {
    }

    @Around("replace()")
    public Register around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Register register = (Register)joinPoint.proceed(args);
        String ip = RealIPUtils.getRealIp();
        log.info("Nacos注册===原IP:{},替换真实IP:{}",register.getIp(),ip);
        register.setIp(ip);
        return register;
    }

}
