package io.pifind.map3rd.google.config;

import com.alibaba.fastjson2.support.spring.http.converter.FastJsonHttpMessageConverter;
import io.pifind.map3rd.error.MapApiCode;
import io.pifind.map3rd.error.ThirdPartMapServiceException;
import io.pifind.map3rd.google.request.GoogleApiTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

import static io.pifind.map3rd.MapApiConstants.PROPERTIES_NAME;

@Configuration
public class GoogleApiTemplateConfiguration {

    public static final String GOOGLE_API_KEY = PROPERTIES_NAME + ".google.key";

    @Autowired
    private Environment environment;

    @Bean
    public GoogleApiTemplate googleApiTemplate() {
        if (environment.containsProperty(GOOGLE_API_KEY)) {
            String key = environment.getProperty(GOOGLE_API_KEY);
            GoogleApiTemplate googleApiTemplate = new GoogleApiTemplate(key);
            // 配置FastJson消息转换器
            googleApiTemplate.getMessageConverters().add(
                    0,
                    new FastJsonHttpMessageConverter()
            );
            // 设置异常处理器
            googleApiTemplate.setErrorHandler(new ResponseErrorHandler() {
                @Override
                public boolean hasError(ClientHttpResponse response) throws IOException {
                    return response.getStatusCode().isError();
                }
                @Override
                public void handleError(ClientHttpResponse response) throws IOException {
                    throw new ThirdPartMapServiceException(
                            MapApiCode.CONNECTION_ERROR,
                            "请求 Google API 异常，异常代码" + response.getStatusCode()
                    );
                }
            });

            return googleApiTemplate;
        }
        throw new RuntimeException("没有配置Google API");
    }

}
