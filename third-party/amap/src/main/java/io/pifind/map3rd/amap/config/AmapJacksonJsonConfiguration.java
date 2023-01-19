package io.pifind.map3rd.amap.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;

@Configuration
public class AmapJacksonJsonConfiguration {

    @Bean("Amap-MappingJackson2HttpMessageConverter")
    private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        // TODO 自定义一个 WrapperMapper
        ObjectMapper wrapperMapper = new ObjectMapper();




        converter.setObjectMapper(wrapperMapper);
        return converter;
    }


}
