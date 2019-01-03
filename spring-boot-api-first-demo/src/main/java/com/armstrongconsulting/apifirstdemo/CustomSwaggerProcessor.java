package com.armstrongconsulting.apifirstdemo;

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

import java.util.Arrays;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import io.swagger.inflector.config.SwaggerProcessor;
import io.swagger.models.Scheme;
import io.swagger.models.Swagger;

/**
 * Will be instantiated by swagger inflector, whenever swagger.json/yaml files are rendered.
 * 
 * Note: Is a candidate for a swagger-inflector PR request so that inflector instantiates it via a factory
 * (similar to controllers) instead of blindly instantiating it via reflection
 *
 */
@Component
public class CustomSwaggerProcessor implements SwaggerProcessor, ApplicationContextAware {

	private static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}

	@Override
	public void process(Swagger swagger) {
		int port = CustomSwaggerProcessor.context.getEnvironment().getProperty("server.port", Integer.class);
		String prefix = CustomSwaggerProcessor.context.getEnvironment().getProperty("spring.jersey.applicationPath", String.class);
		swagger.setHost(String.format("localhost:%d", port));
		swagger.setBasePath(prefix + swagger.getBasePath());
		swagger.setSchemes(Arrays.asList(Scheme.forValue("http")));
	}


}
