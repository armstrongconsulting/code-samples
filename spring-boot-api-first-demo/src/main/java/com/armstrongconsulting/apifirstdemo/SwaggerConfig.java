package com.armstrongconsulting.apifirstdemo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/*
 *  Copyright 2019 Armstrong Consulting Services
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  
 */
import com.armstrongconsulting.apifirstdemo.controllers.PetController;
import com.armstrongconsulting.apifirstdemo.models.Pet;

import io.swagger.inflector.SwaggerInflector;
import io.swagger.inflector.config.Configuration;
import io.swagger.inflector.config.Configuration.Direction;
import io.swagger.inflector.utils.DefaultExceptionMapper;

@org.springframework.context.annotation.Configuration
public class SwaggerConfig {

	@Bean
	Configuration configuration(ApplicationContext applicationContext) {

		List<String> processors = new ArrayList<>();
		processors.add(CustomSwaggerProcessor.class.getName());

		Configuration cfg = new Configuration()
				.controllerPackage(PetController.class.getPackage().getName())
				.modelPackage(Pet.class.getPackage().getName())
				.swaggerUrl(getClass().getResource("/swagger.yaml").getFile())
				.defaultValidators()
				.defaultConverters();

		cfg.setSwaggerProcessors(processors);
		cfg.setEnvironment(Configuration.Environment.DEVELOPMENT);
		cfg.setExceptionMappers(new HashSet<Class<?>>(Arrays.asList(new Class<?>[] { DefaultExceptionMapper.class })));
		cfg.getEntityProcessors().add("json");
		cfg.getEntityProcessors().add("yaml");
		cfg.getEntityProcessors().add("xml");

		cfg.setValidatePayloads(EnumSet.of(Direction.IN, Direction.OUT));
		cfg.setControllerFactory((cls, operation) -> applicationContext.getBean(cls));

		return cfg;
	}

	@Bean
	SwaggerInflector swagger(Configuration configuration) {
		return new SwaggerInflector(configuration);
	}

	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config);
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>(new CorsFilter(source));
		bean.setOrder(Integer.MIN_VALUE + 1);
		return bean;
	}
}
