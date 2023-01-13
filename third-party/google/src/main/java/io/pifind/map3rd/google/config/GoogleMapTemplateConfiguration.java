package io.pifind.map3rd.google.config;

import com.alibaba.fastjson2.support.spring.http.converter.FastJsonHttpMessageConverter;
import io.pifind.map3rd.google.request.GoogleApiTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class GoogleMapTemplateConfiguration {

    public static final String GOOGLE_API_KEY = "map-3rd.google-api-key";

    @Autowired
    private Environment environment;

    @Bean
    public GoogleApiTemplate googleApiTemplate() {
        if (environment.containsProperty(GOOGLE_API_KEY)) {
            String key = environment.getProperty(GOOGLE_API_KEY);
            GoogleApiTemplate googleApiTemplate = new GoogleApiTemplate(key);
            // 配置FastJson消息转换器
            googleApiTemplate.getMessageConverters().add(0,new FastJsonHttpMessageConverter());
            return googleApiTemplate;
        }
        throw new RuntimeException("没有配置Google API");
    }

}
