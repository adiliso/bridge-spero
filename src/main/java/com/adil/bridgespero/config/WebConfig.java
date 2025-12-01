package com.adil.bridgespero.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Value("${spring.application.storage.public-dir}")
  String publicUploadDir;

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry
      .addResourceHandler("/public/**")
      .addResourceLocations("file:" + publicUploadDir)
      .setCachePeriod(3600)
      .resourceChain(true)
      .addResolver(new PathResourceResolver());
  }
}
