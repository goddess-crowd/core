package com.goddess.common.util;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Redis工具类
 * @author 失败女神
 * @email: 18733123202@163.com
 * @date 2021/4/7 上午9:35
 * @Copyright © 女神帮
 */
@Component
@ConditionalOnClass(RedisTemplate.class)
@Slf4j
public class RedisUtils {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisTemplate<String, String> redisStringTemplate;

    @Autowired
    private ValueOperations<String, String> valueOperations;
    @Autowired
    private HashOperations<String, String, Object> hashOperations;
    @Autowired
    private ListOperations<String, Object> listOperations;
    @Autowired
    private SetOperations<String, Object> setOperations;
    @Autowired
    private ZSetOperations<String, Object> zSetOperations;
    /**
     * 默认过期时长，单位：秒
     */
    public final static long DEFAULT_EXPIRE = 60 * 60 * 24;
    /**
     * 不设置过期时长
     */
    public final static long NOT_EXPIRE = -1;
    private final static Gson gson = new Gson();

    public Set<String> keys(String key) {
        return redisTemplate.keys(key);
    }

    public void set(String key, Object value, long expire) {
        valueOperations.set(key, toJson(value));
        if (expire != NOT_EXPIRE) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }
    public void multiSet(Map value) {
        valueOperations.multiSet(value);
    }

    public List<String> multiget(List<String> keys) {
        return valueOperations.multiGet(keys);
    }

    public List<String> getList(String key) {
        return redisStringTemplate.opsForList().range(key, 0, -1);

    }
    public List<String> getList(String key, long start, long end) {
        return redisStringTemplate.opsForList().range(key, start, end);
    }

    /**
     * <p>
     * 通过key给field设置指定的值,如果key不存在,则先创建
     * </p>
     *
     * @param key
     * @param field
     *            字段
     * @param value
     */
    public void hset(String key, String field, String value) {
        hashOperations.put(key,field,value);
    }

    /**
     * <p>
     * 通过key给field 获取value
     * </p>
     *
     * @param key
     * @param field
     *            字段
     */
    public Object hget(String key, String field) {
        return hashOperations.get(key,field);
    }

    /**
     * <p>
     * 通过key同时设置 hash的多个field
     * </p>
     *
     * @param key
     * @param hash
     */
    public void hmset(String key, Map<String, Object> hash) {
        hashOperations.putAll(key,hash);
    }

    /**
     * <p>
     * 批量获取集合中的值
     * </p>
     *
     * @param key
     * @return
     */
    public List<Object> multiGet(String key, Collection<String> hashKeys) {
        return hashOperations.multiGet(key,hashKeys);
    }

    /**
     * <p>
     * 通过key获取所有的field和value
     * </p>
     *
     * @param key
     * @return
     */
    public Map<String, Object> hgetall(String key) {
        return hashOperations.entries(key);
    }
    /**
     * <p>
     * 通过key获取size
     * </p>
     *
     * @param key
     * @return
     */
    public Long size(String key) {
        return hashOperations.size(key);
    }
    /**
     * <p>
     * 通过key 和 field删除
     * </p>
     *
     * @param key
     * @return
     */
    public Long delete(String key, Object... var2) {
        return hashOperations.delete(key, var2);
    }

    /**
     * <p>
     * 为给定 key 设置生存时间，当 key 过期时(生存时间为 0 )，它会被自动删除。
     * </p>
     *
     * @param key
     * @param value
     *            过期时间，单位：秒
     * @return 成功返回true
     */
    public Boolean expire(String key, int value) {
        return redisTemplate.expire(key, value, TimeUnit.SECONDS);
    }

    public <T> List<T> getObjectList(String key,Class<T> clazz){
        List<String> sList = getList(key);
        List<T> tList = Lists.newArrayList();
        tList =sList.stream().map(str ->fromJson(str,clazz)).collect(Collectors.toList());
        return tList;
    }

    public void setStringList(String key, List<String> list) {
        redisStringTemplate.opsForList().rightPushAll(key, list);
    }

    public void set(String key, Object value) {
        set(key, value, DEFAULT_EXPIRE);
    }

    public <T> T get(String key, Class<T> clazz, long expire) {
        String value = valueOperations.get(key);
        if (expire != NOT_EXPIRE) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return value == null ? null : fromJson(value, clazz);
    }

    public <T> T get(String key, Class<T> clazz) {
        return get(key, clazz, NOT_EXPIRE);
    }

    public String get(String key, long expire) {
        String value = valueOperations.get(key);
        if (expire != NOT_EXPIRE) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return value;
    }
    public long increment(String key, long val) {
        return valueOperations.increment(key,val);
    }

    public String get(String key) {
        return get(key, NOT_EXPIRE);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public void deletes(Collection<String> keys) {
        redisTemplate.delete(keys);
    }

    /**
     * 加锁
     * @param key lock key
     * @param value 当前线程操作时的 System.currentTimeMillis() + 450，450是超时时间，这个地方不需要去设置redis的expire，
     *              也不需要超时后手动去删除该key，因为会存在并发的线程都会去删除，造成上一个锁失效，结果都获得锁去执行，并发操作失败了就。
     * @param retryTimes 重试次数
     * @return
     */
    public boolean lock(String key, String value, int retryTimes) {
        //如果key值不存在，则返回 true，且设置 value
        if (redisTemplate.opsForValue().setIfAbsent(key, value)) {
            this.expire(key, (int) DEFAULT_EXPIRE);
            return true;
        }

        //获取key的值，判断是是否超时
        for (int i = 0; i < retryTimes; i++) {
            Object curVal = redisTemplate.opsForValue().get(key);
            if(curVal == null){
                redisTemplate.opsForValue().set(key,value);
                return true;
            }
            if (StringUtils.isNotEmpty(curVal.toString()) && Long.parseLong(curVal.toString()) < System.currentTimeMillis()) {
                //获得之前的key值，同时设置当前的传入的value。这个地方可能几个线程同时过来，但是redis本身天然是单线程的，所以getAndSet方法还是会安全执行，
                //首先执行的线程，此时curVal当然和oldVal值相等，因为就是同一个值，之后该线程set了自己的value，后面的线程就取不到锁了
                Object oldVal = redisTemplate.opsForValue().getAndSet(key, value);
                if(StringUtils.isNotEmpty(oldVal.toString()) && oldVal.equals(curVal)) {
                    return true;
                }
            }
            // 自旋操作
            try {
                log.info("线程" + Thread.currentThread().getName()  + "占用锁失败，自旋等待结果");
                Thread.sleep(10);
            } catch (InterruptedException e) {
                continue;
            }

        }
        return false;
    }

    /**
     * 解锁
     * @param key lock key
     * @param value
     */
    public void unlock(String key, String value) {
        try {
            Object curVal = redisTemplate.opsForValue().get(key);
            if (curVal!=null && curVal.equals(value)) {
                redisTemplate.opsForValue().getOperations().delete(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Object转成JSON数据
     */
    private String toJson(Object object) {
        if (object instanceof Integer || object instanceof Long || object instanceof Float || object instanceof Double
            || object instanceof Boolean || object instanceof String) {
            return String.valueOf(object);
        }
        return gson.toJson(object);
    }

    /**
     * JSON数据，转成Object
     */
    private <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }
}
