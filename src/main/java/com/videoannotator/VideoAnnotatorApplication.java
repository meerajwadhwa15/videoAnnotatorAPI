package com.videoannotator;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "VideoAnnotator API", version = "1.0", description = "A practice project with user password management and video annotation feature"))
public class VideoAnnotatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(VideoAnnotatorApplication.class, args);
    }

}
