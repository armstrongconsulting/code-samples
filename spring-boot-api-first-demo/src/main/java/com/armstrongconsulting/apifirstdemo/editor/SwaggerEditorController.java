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
 *  A sample controller; Since Swagger inflector is running in DEVELOPMENT mode, it will
 *  mock out not implemented controllers with example data.
 *
 */

package com.armstrongconsulting.apifirstdemo.editor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SwaggerEditorController {

	@Value("${server.servlet.context-path}")
	String contextPath;

	@Value("${spring.jersey.applicationPath}")
	String apiBasePath;

	@Value("${server.port}")
	String localPort;

	private static final String REQUEST_PATH = "/swagger-spec";
	private static final String YAML_RESOURCE = "/swagger.yaml";
	private static final Charset UTF8 = Charset.forName("utf-8");


	private String getSpecAbsolutePath() {
		String file;
		URL fileInClasspath = getClass().getResource(YAML_RESOURCE);
		if (fileInClasspath.getProtocol().equals("file")) {
			// Application started from IDE (class files in target/classes),
			// getResource resolves to file in FS
			file = fileInClasspath.getFile().replace("target/classes", "src/main/resources");
		} else {
			// Application started from command line (classes from jar),
			// getResource resolves to file in jar, assumes current working
			// directory is project root
			file = "src/main/resources/swagger.yaml";
			if (!(new File(file).exists())) {
				return null;
			}
		}
		return file;
	}

	@RequestMapping(method = RequestMethod.PUT, path = REQUEST_PATH)
	public void save(@RequestBody String yaml) throws IOException {
		String fileName = getSpecAbsolutePath();
		if (fileName != null) {
			FileUtils.write(new File(fileName), yaml, UTF8);
		}
	}

	@RequestMapping(method = RequestMethod.GET, path = REQUEST_PATH)
	public String load() throws IOException {
		String fileName = getSpecAbsolutePath();
		if (fileName != null) {
			String yaml = FileUtils.readFileToString(new File(fileName), UTF8);
			return yaml;
		} else {
			InputStream yamlStream = getClass().getResourceAsStream(YAML_RESOURCE);
			try {
				return StreamUtils.copyToString(yamlStream, UTF8);
			} finally {
				yamlStream.close();
			}
		}
	}

}
