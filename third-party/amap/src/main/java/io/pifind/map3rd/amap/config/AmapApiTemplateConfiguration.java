package io.pifind.map3rd.amap.config;

import io.pifind.map3rd.amap.request.AmapApiTemplate;
import io.pifind.map3rd.amap.request.AmapWrapperConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import static io.pifind.map3rd.MapApiConstants.PROPERTIES_NAME;

@Configuration
public class AmapApiTemplateConfiguration {

    public static final String KEY_PROPERTY = PROPERTIES_NAME + ".amap.key";
    public static final String SIGN_OPEN_PROPERTY = PROPERTIES_NAME + ".amap.sign.open";
    public static final String SIGN_KEY_PROPERTY = PROPERTIES_NAME + ".amap.sign.key";

    @Autowired
    private Environment environment;

    @Autowired
    private AmapWrapperConverter amapWrapperConverter;

    @Bean
    public AmapApiTemplate amapTemplate() {

        // 获取高德 key 配置
        final String key ;
        if (environment.containsProperty(KEY_PROPERTY)) {
            key = environment.getProperty(KEY_PROPERTY);
        } else {
            throw new RuntimeException("未定义高德地图key值");
        }

        // 获取签名配置
        boolean needSign = false;
        String signKey = null;
        if (environment.containsProperty(SIGN_OPEN_PROPERTY)) {
            needSign = Boolean.TRUE.equals(environment.getProperty(SIGN_OPEN_PROPERTY, Boolean.class));
            if (needSign) {
                // 获取签名密钥
                if (environment.containsProperty(SIGN_KEY_PROPERTY)) {
                    signKey = environment.getProperty(SIGN_KEY_PROPERTY);
                } else {
                    throw new RuntimeException("未定义高德地图签名密钥");
                }
            }
        }

        // 创建一个模板
        AmapApiTemplate amapTemplate = new AmapApiTemplate(key,signKey,needSign);

        // 设置自定义的信息转换器
        amapTemplate.getMessageConverters().set(0,amapWrapperConverter);

        // 返回模板
        return amapTemplate;
    }

}
