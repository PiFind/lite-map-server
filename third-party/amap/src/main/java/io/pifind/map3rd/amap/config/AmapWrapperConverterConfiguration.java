package io.pifind.map3rd.amap.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import io.pifind.map3rd.amap.request.AmapWrapperConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmapWrapperConverterConfiguration {

    @Bean
    public AmapWrapperConverter amapWrapperConverter() {
        AmapWrapperConverter amapWrapperConverter = new AmapWrapperConverter();
        amapWrapperConverter.getObjectMapper().enable(
                DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT,
                DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT
        );
        return amapWrapperConverter;
    }

}
