package movieapp.webmovie.config;

import movieapp.webmovie.security.JwtAuthenticationFilter;
import movieapp.webmovie.security.JwtTokenUtil;
import movieapp.webmovie.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtTokenUtil jwtUtil, UserService userService) {
        return new JwtAuthenticationFilter(jwtUtil, userService);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        // ✅ Các route public
                        .requestMatchers(
                                "/api/auth/**",
                                "/api/paypal/**",
                                "/api/payments/webhook",
                                "/success",
                                "/cancel",
                                "/uploads/**",
                                "/api/paypal/capture-order",
                                "/api/paypal/paypal-success")
                        .permitAll()

                        // ✅ Gửi thông báo chỉ cho ADMIN
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/notifications/**").hasRole("ADMIN")

                        // ✅ Xem và cập nhật thông báo cho USER, ADMIN, STAFF
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/notifications/**")
                        .hasAnyRole("USER", "ADMIN", "STAFF")
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/notifications/**")
                        .hasAnyRole("USER", "ADMIN", "STAFF")

                        // ✅ Cho phép xem gói
                        .requestMatchers("/api/plans/**").hasAnyRole("USER", "ADMIN")

                        .requestMatchers("/api/payments/me").hasAnyRole("USER", "ADMIN")

                        .requestMatchers("/api/payments/user/**", "/api/payments/all").hasRole("ADMIN")

                        // ✅ Các route còn lại bắt buộc phải đăng nhập
                        .anyRequest().authenticated())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}