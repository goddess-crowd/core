package com.goddess.common.util;

import cn.hutool.extra.spring.SpringUtil;
import com.goddess.common.exception.AppException;
import com.goddess.common.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @Description:生成项目全局的IdWorder对象
 * @author 失败女神
 * @email: 18733123202@163.com
 * @date 2021/4/7 上午9:35
 * @Copyright © 女神帮
 */
@Slf4j
public class IdWorkerFactory {
    private static Map<String, SnowflakeIdWorker> SnowflakeIdWorkerMap = new HashMap<>();
    private static final Object LOCK = new Object();
    /**
     * 支持32个机器
     */
    private static final int maxWorkerId = 31;
    /**
     * 支持32个中心
     */
    private static final int maxDatacenterId = 31;
    private static long workId = getWorkerId();
    private static long datacenterId = getDataCenterId();

    /**
     * 根据名称获取BizCenterNoWorker对象
     */
    public static CenterNoWorker getBizCenterNoWorker() {
        CenterNoWorker bizCenterNoWorker = SpringUtil.getBean(CenterNoWorker.class);
        if (bizCenterNoWorker == null) {
            throw new AppException("请先注入BizCenterNoWorker", "500");
        }
        return bizCenterNoWorker;
    }

    /**
     * 根据中心和名称获取SnowflakeIdWorker对象
     *
     * @param name id对应的名称
     */
    public static SnowflakeIdWorker getSnowflakeIdWorker(String name) {
        SnowflakeIdWorker snowflakeIdWorker = SnowflakeIdWorkerMap.get(name);
        if (null == snowflakeIdWorker) {
            synchronized (LOCK) {
                snowflakeIdWorker = SnowflakeIdWorkerMap.get(name);
                if (null == snowflakeIdWorker && SpringUtil.getApplicationContext()!=null) {
                    snowflakeIdWorker = SpringUtil.getBean(SnowflakeIdWorker.class);
                    SnowflakeIdWorkerMap.put(name, snowflakeIdWorker);
                }else {
                    snowflakeIdWorker = new SnowflakeIdWorker();
                }
            }
        }
        return snowflakeIdWorker ;
    }

    /**
     * 获取机器标识
     */
    public static long getWorkerId() {
        return getMachineNum();
    }

    /**
     * 根据机器生成中心标识
     */
    public static long getDataCenterId() {
        String hostName = getHostName();
        if (StringUtils.isNotBlank(hostName)) {
            return (long)hostName.hashCode() & maxDatacenterId;
        }
        return new Random().nextInt(maxDatacenterId);
    }

    /**
     * 获取机器编码
     */
    private static long getMachineNum() {
        long machinePiece;
        StringBuilder sb = new StringBuilder();
        Enumeration<NetworkInterface> e = null;
        try {
            e = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e1) {
            e1.printStackTrace();
        }
        while (e.hasMoreElements()) {
            NetworkInterface ni = e.nextElement();
            sb.append(ni.toString());
        }
        machinePiece = sb.toString().hashCode();
        return machinePiece & maxDatacenterId;
    }

    private static String getHostName() {
        try {
            InetAddress netAddress = InetAddress.getLocalHost();
            if (null != netAddress) {
                String hostName = netAddress.getHostName();
                if (hostName != null && hostName.contains("-")) {
                    hostName = hostName.replaceAll("-", "");
                }
                return hostName;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(getHostName());
        System.out.println(getWorkerId());
        System.out.println(getDataCenterId());
    }
}
