package khuong.com.lasttermjava.utils;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ConfigCloundinary {

    @Bean
    public Cloudinary configKey() {
        try {


            Map<String, String> config = new HashMap<>();

            config.put("cloud_name", "dhp7ylyvh");
            config.put("api_key", "243273335899587");
            config.put("api_secret", "mw1tC0PLGBtkv_9Ypg2o5x7fD6A");
            return new Cloudinary(config);
        } catch (Exception e) {
            throw new RuntimeException("Cloudinary configuration error", e);
        }
    }
}
