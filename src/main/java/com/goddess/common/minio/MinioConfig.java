package com.goddess.common.minio;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * @author 失败女神
 * @email: 18733123202@163.com
 * @date 2021/4/12 下午10:29
 * @Copyright © 女神帮
 */
@Configuration
@EnableConfigurationProperties(MinioProp.class)
@ConditionalOnProperty(prefix = "goddess", name = "minio.enable", havingValue = "true")
public class MinioConfig {

    @Autowired
    private MinioProp minioProp;

    /**
     * 获取MinioClient
     */
    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(minioProp.getEndpoint())
                .credentials(minioProp.getAccessKey(), minioProp.getSecretKey())
                .build();
    }

}