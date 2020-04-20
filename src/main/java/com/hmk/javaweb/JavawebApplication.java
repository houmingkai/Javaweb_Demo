package com.hmk.javaweb;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@NacosPropertySource(dataId = "springboot2-nacos-config", autoRefreshed = true)
public class JavawebApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavawebApplication.class, args);
    }

}
