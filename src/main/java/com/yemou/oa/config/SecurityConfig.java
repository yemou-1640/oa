//package com.yemou.oa.config;
//
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
///**
// * Spring Security配置类
// */
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity
//public class SecurityConfig {
//
//    /**
//     * 密码编码器
//     */
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    /**
//     * 安全过滤器链
//     */
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//            .authorizeHttpRequests((authz) -> authz
//                // 允许静态资源访问
//                .requestMatchers("/layuiadmin/**", "/favicon.ico").permitAll()
//                // 登录页允许访问
//                .requestMatchers("/user/toLogin", "/user/login").permitAll()
//                // 管理员专属路径
//                .requestMatchers("/admin/**").hasRole("ADMIN")
//                // 用户路径
//                .requestMatchers("/myapp/**").hasAnyRole("ADMIN", "USER")
//                // 其他请求需要认证
//                .anyRequest().authenticated()
//            )
//            .formLogin(form -> form
//                .loginPage("/user/toLogin")
//                .loginProcessingUrl("/user/login")
//                .defaultSuccessUrl("/toIndex")
//                .failureUrl("/user/toLogin?error")
//            )
//            .logout(logout -> logout
//                .logoutUrl("/user/logout")
//                .logoutSuccessUrl("/user/toLogin")
//            ).exceptionHandling(e -> e
//                .accessDeniedPage("/erro/403")
//                ).csrf(csrf -> csrf.disable());
//
//        return http.build();
//    }
//}