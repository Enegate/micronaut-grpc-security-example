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

import com.enegate.micronaut.grpc.proto.example.GreeterGrpc;
import com.enegate.micronaut.grpc.proto.example.HelloReply;
import com.enegate.micronaut.grpc.proto.example.HelloRequest;
import com.enegate.micronaut.grpc.security.GrpcSecurityContext;
import com.enegate.micronaut.grpc.server.annotation.GrpcService;
import com.enegate.micronaut.grpc.server.example.interceptor.GreeterServiceInterceptor;
import io.grpc.Context;
import io.grpc.stub.StreamObserver;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Steve Schneider
 */

@GrpcService(interceptors = {GreeterServiceInterceptor.class})
@Secured(SecurityRule.IS_AUTHENTICATED)
public class GreeterService extends GreeterGrpc.GreeterImplBase {

    private static final Logger LOG = LoggerFactory.getLogger(GreeterService.class);

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        LOG.info("gRPC service called: " + GreeterGrpc.getSayHelloMethod().getFullMethodName());
        LOG.info("Authenticated User: " + GrpcSecurityContext.getAuthentication().getName());
        HelloReply reply = HelloReply.newBuilder().setMessage("Hello " + request.getName()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    @Secured(SecurityRule.IS_ANONYMOUS)
    public void sayHelloAnonymous(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        LOG.info("gRPC service called: " + GreeterGrpc.getSayHelloAnonymousMethod().getFullMethodName());
        HelloReply reply = HelloReply.newBuilder().setMessage("Hello " + request.getName()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    @Secured({"ROLE_ADMIN", "ROLE_X"})
    public void sayHelloRoles(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        LOG.info("gRPC service called: " + GreeterGrpc.getSayHelloRolesMethod().getFullMethodName());
        LOG.info("Authenticated User: " + GrpcSecurityContext.getAuthentication().getName());
        HelloReply reply = HelloReply.newBuilder().setMessage("Hello " + request.getName()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
