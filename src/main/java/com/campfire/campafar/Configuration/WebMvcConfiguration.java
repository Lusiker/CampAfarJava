package com.campfire.campafar.Configuration;

import com.campfire.campafar.Utils.FileProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.File;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //将获取 /files/avatar/** 的请求映射至 ../avatar/
        File avatarPath = new File(FileProcessor.avatarStorePath);
        File articlePath = new File(FileProcessor.articleStorePath);

        registry.addResourceHandler("/files/avatar/**")
                .addResourceLocations("file:" + avatarPath.getAbsolutePath() + "\\")
                .setCachePeriod(3600)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());

        registry.addResourceHandler("/files/article/**")
                .addResourceLocations("file:" + articlePath.getAbsolutePath() + "\\")
                .setCachePeriod(3600)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowCredentials(true)
                .allowedMethods("*")
                .maxAge(3600);
    }
}
