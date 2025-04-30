package com.lhl.rp.config;

import com.lhl.rp.filter.JwtAuthenticationFilter;
import com.lhl.rp.handler.MyAccessDeniedHandler;
import com.lhl.rp.handler.MyAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/3/26_23:18
 */

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    /**
     * 注入密码加密器
     * 彩虹表
     *
     * @return BCryptPasswordEncoder
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 注入认证管理器
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(userDetailsService); // 通过构造注入
    }

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private MyAuthenticationEntryPoint entryPoint;

    @Autowired
    private MyAccessDeniedHandler accessDeniedHandler;

    @Autowired
    private CorsConfig corsConfigSource;

    /**
     * Spring Security 核心配置
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 禁用跨站请求伪造
                .csrf(AbstractHttpConfigurer::disable)
                // 跨域请求配置
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // 禁用 Session
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 放行登录接口（业务接口都在二级路径下，一级路径默认 public）
                        .requestMatchers("/api/*").permitAll()
                        // 其他接口需要认证
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                // 配置异常处理
                .exceptionHandling(exception -> exception
                        // 认证失败处理器（未登录）
                        .authenticationEntryPoint(entryPoint)
                        // 权限不足处理器（禁止访问）
                        .accessDeniedHandler(accessDeniedHandler)
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        if (corsConfigSource.getAllowedOrigins().contains("*") && Boolean.TRUE.equals(corsConfigSource.getAllowCredentials())) {
            throw new IllegalStateException("Cannot set allowCredentials to true when allowedOrigins contains '*'.");
        }
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(corsConfigSource.getAllowedOrigins());
        configuration.setAllowedMethods(corsConfigSource.getAllowedMethods());
        configuration.setAllowedHeaders(corsConfigSource.getAllowedHeaders());
        configuration.setAllowCredentials(corsConfigSource.getAllowCredentials());

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
