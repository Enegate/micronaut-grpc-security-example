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

package com.enegate.micronaut.grpc.server.example.interceptor;

import com.enegate.micronaut.grpc.server.GrpcServerBuilderInterceptor;
import com.enegate.micronaut.grpc.server.annotation.GrpcInterceptor;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Steve Schneider
 */

@GrpcInterceptor
public class ExampleServerBuilderInterceptor implements GrpcServerBuilderInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(ExampleServerBuilderInterceptor.class);

    @Override
    public void intercept(ServerBuilder serverBuilder) {
        LOG.info("ServerBuilder type: {}", serverBuilder.getClass().getName());
        // To configure e.g. TLS
//        serverBuilder.useTransportSecurity(...);
    }
}
