package com.refuge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.refuge.config.FileStorageProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        FileStorageProperties.class
})
public class RefugeAnimauxApplication {

    public static void main(String[] args) {
        SpringApplication.run(RefugeAnimauxApplication.class, args);
    }
}
