package com.campusstudy.grupos.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    // Auth se maneja con AuthFilter (OncePerRequestFilter)
}
