package com.goddess.common.util;

import com.goddess.common.exception.AppException;
import com.goddess.common.exception.AppExceptionHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

/**
 * @author 失败女神
 * @email: 18733123202@163.com
 * @date 2021/4/7 上午9:35
 * @Copyright © 女神帮
 */
@Component
public class CenterNoWorker {

    @Value("${spring.redis.host:}")
    private String redisHost;
    @Value("${env:dev}")
    private String env;
    @Autowired(required = false)
    private StringRedisTemplate stringRedisTemplate;
    @Autowired(required = false)
    private AppExceptionHandler appExceptionHandler;


    // ==============================Methods==========================================
    /**
     * 获得下一个业务流程Code
     * @return SnowflakeId
     */
    public String nextCode(String bizCode) {
        if (StringUtils.isNotBlank(redisHost)) {
            String yyyyMMdd = LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE);
            String dateKey = RedisKeys.getPermsKey(bizCode+":"+env+":"+yyyyMMdd);
            Long increment = stringRedisTemplate.opsForValue().increment(dateKey, 1L);
            if(Objects.equals(1L,increment)){
                //删除昨天key
                stringRedisTemplate.delete(RedisKeys.getPermsKey(bizCode+":"+env+":"+LocalDateTime.now().minusDays(1).format(DateTimeFormatter.BASIC_ISO_DATE)));
            }
            return String.format(bizCode+yyyyMMdd+"%06d", increment);

        }else{
            throw new AppException("请先开启redis","sys." + appExceptionHandler.getServe() + ".500");
        }

    }
    /**
     * 生成业务编号,店铺、用户等
     * @return SnowflakeId
     */
    public String nextCodeNoFlow(String bizCode) {
        return this.nextCodeNoFlow(bizCode,null);
    }

    /**
     * 生成业务编号,店铺、用户等
     *
     * num: 生成序号的位数
     *
     * @return SnowflakeId
     */
    public String nextCodeNoFlow(String bizCode,Integer num) {

        Integer integer = Optional.ofNullable(num).orElse(6);

        if (StringUtils.isNotBlank(redisHost)) {
            String dateKey = RedisKeys.getPermsKey(bizCode+":"+env);
            Long increment = stringRedisTemplate.opsForValue().increment(dateKey, 1L);
            return String.format(bizCode+"%0"+integer+"d", increment);

        }else{
            throw new AppException("请先开启redis","sys." + appExceptionHandler.getServe() + ".500");
        }

    }

    public static void main(String[] args) {
        System.out.println( LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE));
    }

}
