package com.priceline.role.config;

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class MessageConfig {
	
	@Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasename("internationalization/messages");
        source.setDefaultLocale(Locale.ENGLISH);
        source.setDefaultEncoding("UTF-8");

        return source;
    }

}
