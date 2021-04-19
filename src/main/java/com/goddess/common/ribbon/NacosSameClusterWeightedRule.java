package com.goddess.common.ribbon;


import com.alibaba.boot.nacos.discovery.properties.NacosDiscoveryProperties;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.goddess.common.util.RealIPUtils;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * @author 失败女神
 * @email: 18733123202@163.com
 * @date 2021/4/11 上午2:10
 * @Copyright © 女神帮
 */
@Slf4j
public class NacosSameClusterWeightedRule extends AbstractLoadBalancerRule {

    @Value("${goddess.ip-replace.enable:false}")
    private boolean enable;
    @Value("${goddess.ip-replace.num:100}")
    private int num;
    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {
        //读取配置文件，并初始化
    }

    @Override
    public Server choose(Object o) {
        try {
            // 拿到配置文件中的集群名称 
            String clusterName = nacosDiscoveryProperties.getClusterName();
            BaseLoadBalancer loadBalancer = (BaseLoadBalancer) this.getLoadBalancer();
            // 想要请求的微服务的名称
            String name = loadBalancer.getName();
//            // 拿到服务发现的相关API
//            NamingService namingService = nacosDiscoveryProperties.namingServiceInstance();
            Properties properties = new Properties();
//            properties.setProperty(PropertyKeyConst.IS_USE_CLOUD_NAMESPACE_PARSING, nacosDiscoveryProperties.getServerAddr());
            if(StringUtils.isNotBlank(nacosDiscoveryProperties.getEndpoint())){
                properties.setProperty(PropertyKeyConst.ENDPOINT, nacosDiscoveryProperties.getEndpoint());
            }
            if(StringUtils.isNotBlank(nacosDiscoveryProperties.getUsername())){
                properties.setProperty(PropertyKeyConst.USERNAME, nacosDiscoveryProperties.getUsername());
            }
            if(StringUtils.isNotBlank(nacosDiscoveryProperties.getPassword())){
                properties.setProperty(PropertyKeyConst.PASSWORD, nacosDiscoveryProperties.getPassword());
            }
            if(StringUtils.isNotBlank(nacosDiscoveryProperties.getAccessKey())){
                properties.setProperty(PropertyKeyConst.ACCESS_KEY, nacosDiscoveryProperties.getAccessKey());
            }
            if(StringUtils.isNotBlank(nacosDiscoveryProperties.getSecretKey())){
                properties.setProperty(PropertyKeyConst.SECRET_KEY, nacosDiscoveryProperties.getSecretKey());
            }
            if(StringUtils.isNotBlank(nacosDiscoveryProperties.getServerAddr())){
                properties.setProperty(PropertyKeyConst.SERVER_ADDR, nacosDiscoveryProperties.getServerAddr());
            }
            if(StringUtils.isNotBlank(nacosDiscoveryProperties.getContextPath())){
                properties.setProperty(PropertyKeyConst.CONTEXT_PATH, nacosDiscoveryProperties.getContextPath());
            }
            if(StringUtils.isNotBlank(nacosDiscoveryProperties.getClusterName())){
                properties.setProperty(PropertyKeyConst.CLUSTER_NAME, nacosDiscoveryProperties.getClusterName());
            }








//            properties.setProperty(PropertyKeyConst.IS_USE_ENDPOINT_PARSING_RULE, null);
//            properties.setProperty(PropertyKeyConst.ENDPOINT_PORT, null);
//            properties.setProperty(PropertyKeyConst.NAMESPACE, null);
//            properties.setProperty(PropertyKeyConst.RAM_ROLE_NAME,null);
//            properties.setProperty(PropertyKeyConst.ENCODE, null);
//            properties.setProperty(PropertyKeyConst.CONFIG_LONG_POLL_TIMEOUT, null);
//            properties.setProperty(PropertyKeyConst.CONFIG_RETRY_TIME, null);
//            properties.setProperty(PropertyKeyConst.MAX_RETRY, null);
//            properties.setProperty(PropertyKeyConst.ENABLE_REMOTE_SYNC_CONFIG, null);
//            properties.setProperty(PropertyKeyConst.NAMING_LOAD_CACHE_AT_START, null);
//            properties.setProperty(PropertyKeyConst.NAMING_CLIENT_BEAT_THREAD_COUNT, null);
//            properties.setProperty(PropertyKeyConst.NAMING_POLLING_THREAD_COUNT, null);
//            NacosNamingService
            NamingService namingService = NamingFactory.createNamingService(properties);
            // 1. 找到指定服务的所有实例 A
            List<Instance> instances = namingService.selectInstances(name, true);
            // 2. 过滤出相同集群下的所有实例 B
            List<Instance> sameClusterInstances = instances.stream()
                    .filter(instance -> Objects.equals(instance.getClusterName(), clusterName))
                    .collect(Collectors.toList());
            // 3. 如果B是空，就用A
            List<Instance> instancesToBeChosen = new ArrayList<>();
            if (CollectionUtils.isEmpty(sameClusterInstances)) {
                instancesToBeChosen = instances;
//                log.warn("发生跨集群的调用, name = {}, clusterName = {}, instances = {}",
//                        name,
//                        clusterName,
//                        instances
//                );
            } else {
                instancesToBeChosen = sameClusterInstances;
            }
            // 4. 基于权重的负载均衡算法，返回1个实例
            Instance instance = ExtendBalancer.getHostByRandomWeight2(instancesToBeChosen);
//            log.info("选择的实例是 port = {}, instance = {}", instance.getPort(), instance);
            return instance2Server(instance);
        } catch (NacosException e) {
            log.error("发生异常了", e);
            return null;
        }
    }
    private Server instance2Server(Instance instance){
        String instanceIp = instance.getIp();
        int instancePort = instance.getPort();
        if(enable){
            String host = RealIPUtils.getRealIp();
            if(!instance.getIp().equals(host)){
                instanceIp = host;
                instancePort += num;
            }
            log.info("ip 调用替换");
        }
        Server server = new Server(instanceIp,instancePort);
        server.setAlive(true);
//        server.setId();
//        server.setReadyToServe();
//        server.setSchemea();
//        server.setZone();
//        Server.MetaInfo metaInfo = server.getMetaInfo();
//        server.setMetaInfo(instance.getMetadata());
//        server.setId(instance.get);
//        server.setReadyToServe();
        return server;
    }
}