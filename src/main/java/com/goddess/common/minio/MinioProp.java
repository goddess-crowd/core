package com.goddess.common.minio;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
/**
 * @author 失败女神
 * @email: 18733123202@163.com
 * @date 2021/4/12 下午10:22
 * @Copyright © 女神帮
 */

@Data
@Component
@ConditionalOnProperty(prefix = "goddess", name = "minio.enable", havingValue = "true")
@ConfigurationProperties(prefix = "goddess.minio")
public class MinioProp {
    /**
     * 连接地址
     */
    private String endpoint;
    /**
     * 用户名
     */
    private String accessKey;
    /**
     * 密码
     */
    private String secretKey;
    /**
     * 域名
     */
    private String filHost;
}