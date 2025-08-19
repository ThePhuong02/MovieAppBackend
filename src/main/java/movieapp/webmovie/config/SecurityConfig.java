package movieapp.webmovie.config;

import movieapp.webmovie.security.JwtAuthenticationFilter;
import movieapp.webmovie.security.JwtTokenUtil;
import movieapp.webmovie.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

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
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        // ✅ Cho phép OPTIONS request cho CORS preflight
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // ✅ Các route public
                        .requestMatchers(
                                "/api/auth/**",
                                "/api/paypal/**",
                                "/api/payments/webhook",
                                "/success",
                                "/cancel",
                                "/uploads/**",
                                "/api/paypal/capture-order",
                                "/api/paypal/paypal-success",
                                "/api/video/upload")
                        .permitAll()

                        // ✅ API play phim -> phải đăng nhập
                        .requestMatchers("/api/movies/*/stream").hasAnyRole("USER", "ADMIN")

                        // ✅ API upload avatar cho user đã login
                        .requestMatchers(HttpMethod.PUT, "/api/auth/upload-avatar")
                        .hasAnyRole("USER", "ADMIN", "STAFF")

                        // ✅ Gửi thông báo chỉ cho ADMIN
                        .requestMatchers(HttpMethod.POST, "/notifications/**").hasRole("ADMIN")

                        // ✅ Xem và cập nhật thông báo cho USER, ADMIN, STAFF
                        .requestMatchers(HttpMethod.GET, "/notifications/**").hasAnyRole("USER", "ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.PUT, "/notifications/**").hasAnyRole("USER", "ADMIN", "STAFF")

                        // ✅ Cho phép xem gói
                        .requestMatchers("/api/plans/**").hasAnyRole("USER", "ADMIN")

                        .requestMatchers("/api/payments/me").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/payments/user/**", "/api/payments/all").hasRole("ADMIN")

                        // ✅ Các route còn lại bắt buộc login
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
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "http://127.0.0.1:3000",
                "http://localhost:3001",
                "http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setExposedHeaders(List.of("Accept-Ranges", "Content-Range", "Content-Length", "Content-Type"));
        config.setMaxAge(3600L); // Cache preflight for 1 hour

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
