/*
 * Copyright 2018 Enegate
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.enegate.micronaut.grpc.server.example;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

import java.security.Principal;

/**
 * @author Steve Schneider
 */

@Controller("/")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class HelloController {

    @Get("/hello/{name}")
    public String hello(String name, Principal principal) {
        return "Hello " + name + ". Authenticated User: " + principal.getName();
    }

    @Get("/helloanonymous/{name}")
    @Secured(SecurityRule.IS_ANONYMOUS)
    public String helloanonymous(String name) {
        return "Hello " + name;
    }
}
