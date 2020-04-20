package br.com.dmtech.client.config;

import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

@Configuration
public class JacksonConfig {
	public JacksonConfig(ObjectMapper objectMapper) {
		objectMapper.setFilterProvider(new SimpleFilterProvider().setFailOnUnknownId(false));
	}
}