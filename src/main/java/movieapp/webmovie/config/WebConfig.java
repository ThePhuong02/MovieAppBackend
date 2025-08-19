package movieapp.webmovie.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // ✅ Cho phép truy cập ảnh tĩnh ở /uploads/** và không cache
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + System.getProperty("user.dir") + "/uploads/")
                .setCachePeriod(0); // Tránh cache ảnh cũ
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "http://127.0.0.1:3000", "http://localhost:3001")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(true)
                .exposedHeaders("Accept-Ranges", "Content-Range", "Content-Length", "Content-Type")
                .maxAge(3600); // Cache preflight response for 1 hour
    }
}
