package com.botthebuilder.userproject;

import com.botthebuilder.userproject.config.FileUploadProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties({
        FileUploadProperties.class
})
@SpringBootApplication
public class UserProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserProjectApplication.class, args);
    }

}
