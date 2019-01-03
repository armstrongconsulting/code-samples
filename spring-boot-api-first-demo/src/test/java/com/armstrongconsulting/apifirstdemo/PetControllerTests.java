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
package com.armstrongconsulting.apifirstdemo;

import static io.restassured.RestAssured.given;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import com.armstrongconsulting.apifirstdemo.models.Pet;

import io.restassured.RestAssured;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PetControllerTests {

	@LocalServerPort
	protected int port;

	@Before
	public void setup() {

		RestAssured.port = port;
		RestAssured.baseURI = "http://localhost/api/v2";
	}

	@Test
	public void fetchPetTest() {

		/* @formatter:off */
        Pet pet = given()
            .contentType("application/json") 
        .when()
            .get("/pet/12345")
        .then().assertThat()
            .statusCode(200)
            .and().extract().jsonPath().getObject("",Pet.class);
        /* @formatter:on */

		Assert.assertNotNull(pet);
		Assert.assertEquals(Long.valueOf(12345L), pet.getId());
		Assert.assertEquals("Demo Pet", pet.getName());

	}

}

