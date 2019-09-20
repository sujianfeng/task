package com.len.task.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author pk
 * @since 2017/5/10.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                // 选择那些路径和api会生成document
                .select()
                // 对所有api进行监控
                //.apis(RequestHandlerSelectors.any())
                //swagger扫描指定package
                .apis(RequestHandlerSelectors.basePackage("com.len.task.service.controller"))
                // 对所有路径进行监控
                .paths(path -> !"/error".equals(path))
                .build()
                .securitySchemes(newArrayList(new BasicAuth("test")))
                .enable(true);
    }

    @Bean
    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("task接口文档")
                .description("")
                .contact(new Contact("", "", ""))
                .version("1.0")
                .build();
    }
}
