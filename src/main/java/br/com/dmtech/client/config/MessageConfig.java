package br.com.dmtech.client.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class MessageConfig {

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSourceReloadble = new ReloadableResourceBundleMessageSource();
		messageSourceReloadble.setBasenames("classpath:validationMessages", "classpath:messages");
		messageSourceReloadble.setDefaultEncoding("UTF-8");
		messageSourceReloadble.setUseCodeAsDefaultMessage(true);
		return messageSourceReloadble;
	}

}
