package movieapp.webmovie.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dhj3ragzj"); // tên cloud của bạn
        config.put("api_key", "336543582882288");
        config.put("api_secret", "pICeG3Fk-q34eBIinMYeiciNpgE"); // thay YOUR_API_SECRET bằng API Secret thực tế
        config.put("secure", "true");

        return new Cloudinary(config);
    }
}
