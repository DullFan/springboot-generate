package com.dullfan.generate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*")); // 允许任何域名使用
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // 允许任何方法（HTTP 请求类型）
        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type")); // 允许任何请求头
        configuration.setAllowCredentials(true); // 允许证书跨域
        configuration.setMaxAge(3600L); // 预检请求的有效期
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 对所有URL生效
        return source;
    }
}
