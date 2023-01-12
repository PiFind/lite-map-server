package io.pifind.mapserver.config;

import io.pifind.common.i18n.MessageBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;

import static org.apache.commons.codec.CharEncoding.UTF_8;

@Configuration
public class MessageBundleConfiguration {

    public static final String MESSAGE_FILENAME = "i18n/PlaceService";

    @Autowired
    private Environment environment;

    @Bean("PlaceService-MessageBundle")
    private MessageBundle messageBundle() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename(MESSAGE_FILENAME);
        messageSource.setDefaultEncoding(UTF_8);
        return new MessageBundle(messageSource);
    }

}
