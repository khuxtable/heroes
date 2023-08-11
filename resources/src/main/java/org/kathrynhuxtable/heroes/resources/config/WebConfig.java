/*
 * Copyright 2002-2018 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.kathrynhuxtable.heroes.resources.config;

import java.util.List;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "org.kathrynhuxtable.heroes.resources")
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
		builder.modulesToInstall(new JavaTimeModule()).failOnUnknownProperties(true)
				.featuresToDisable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		converters.add(new MappingJackson2HttpMessageConverter(builder.build()));
		converters.add(new StringHttpMessageConverter());
		converters.add(new ByteArrayHttpMessageConverter());
	}

//	@Bean
//	public LocalValidatorFactoryBean validator() {
//		return new LocalValidatorFactoryBean();
//	}

//	@Bean
//	public MethodValidationPostProcessor methodValidationPostProcessor() {
//		return new MethodValidationPostProcessor();
//	}

}