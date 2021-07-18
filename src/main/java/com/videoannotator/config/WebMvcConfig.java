package com.videoannotator.config;

import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        var localeInterceptor = new UrlLocaleInterceptor();
        registry.addInterceptor(localeInterceptor).addPathPatterns("/en/*", "/vi/*");
    }

    @Bean(name = "messageSource")
    public MessageSource getMessageResource() {
        var messageResource = new ReloadableResourceBundleMessageSource();
        messageResource.setBasename("classpath:i18n/messages");
        messageResource.setDefaultEncoding("UTF-8");
        return messageResource;
    }

    @Bean(name = "localeResolver")
    public LocaleResolver getLocaleResolver() {
        return new UrlLocaleResolver();
    }

    @Bean
    public HttpTraceRepository httpTraceRepository()
    {
        return new InMemoryHttpTraceRepository();
    }
}