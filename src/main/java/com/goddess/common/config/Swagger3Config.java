package com.goddess.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author 失败女神
 * @email: 18733123202@163.com
 * @date 2021/4/12 下午6:35
 * @Copyright © 女神帮
 */
@Configuration
public class Swagger3Config {
    @Value("${goddess.swagger.basePackage:com.goddess.center}")
    private String basePackage;
    @Value("${goddess.swagger.title:女神帮服务}")
    private String title;
    @Value("${goddess.swagger.description:女神帮}")
    private String description;
    @Value("${goddess.swagger.contact.name:失败女神}")
    private String contactName;
    @Value("${goddess.swagger.contact.url:https://blog.csdn.net/qq_30615201/article/details/90747742}")
    private String contactUrl;
    @Value("${goddess.swagger.contact.email:18733123202@163.com}")
    private String contactEmail;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .select()
//                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .apis( RequestHandlerSelectors.basePackage(basePackage))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(title)
                .description(description)
                .contact(new Contact(contactName, contactUrl, contactEmail))
                .version("1.0")
                .build();
    }

}