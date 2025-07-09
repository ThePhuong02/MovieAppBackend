package movieapp.webmovie.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// ModelMapper : dùng để ánh xạ các thuộc tính giữa hai object một cách tự động
@Configuration
// Đánh dấu class ModelMapperConfig là class cấu hình của Spring.
// Khi Spring khởi động, nó sẽ quét các class được đánh dấu @Configuration
// và xử lý như một nguồn cấu hình cho Spring IoC Container.
// @Configuration → Đánh dấu class chứa bean.

public class ModelMapperConfig {
   // @Bean: báo với Spring rằng phương thức này trả về một đối tượng cần quản lý (bean)
    // trong Spring Container.
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
