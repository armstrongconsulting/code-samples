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
package com.armstrongconsulting.apifirstdemo.controllers;

import org.springframework.stereotype.Component;

import com.armstrongconsulting.apifirstdemo.models.Pet;

import io.swagger.inflector.models.RequestContext;
import io.swagger.inflector.models.ResponseContext;

@Component
public class PetController{

    public ResponseContext getPetById(RequestContext request, Long petId){
        Pet pet = new Pet().id(petId).name("Demo Pet").addPhotoUrlsItem("http://example.com/sample-photo.jpg");
        return new ResponseContext().status(200).entity(pet);
    }
}