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

package com.enegate.micronaut.grpc.client.example;

import com.enegate.micronaut.grpc.proto.example.GreeterGrpc;
import com.enegate.micronaut.grpc.proto.example.HelloReply;
import com.enegate.micronaut.grpc.proto.example.HelloRequest;
import io.grpc.*;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.reactivex.Flowable;

import java.net.URL;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import static io.micronaut.http.HttpRequest.POST;
import static java.nio.charset.StandardCharsets.UTF_8;

public class Main {
    public static void main(String[] args) throws Exception {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8081).usePlaintext().build();

        callSayHello_Authenticated(channel);
        System.out.println();
        callSayHello_Unauthenticated(channel);
        System.out.println();

        callSayHelloAnonymous_Authenticated(channel);
        System.out.println();
        callSayHelloAnonymous_Unauthenticated(channel);
        System.out.println();

        callSayHelloRoles_Authenticated_ROLE_ADMIN(channel);
        System.out.println();
        callSayHelloRoles_Authenticated_NO_ROLE(channel);
        System.out.println();

        callSayHello_Authenticated_JWT(channel);
        System.out.println();
        callSayHello_Unauthenticated_JWT(channel);
        System.out.println();

        callSayHello_Authenticated_ROLE_ADMIN_JWT(channel);
        System.out.println();
        callSayHello_Authenticated_NO_ROLE_JWT(channel);

        try {
            channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private static void callSayHello_Authenticated_NO_ROLE_JWT(ManagedChannel channel) throws Exception{
        RxHttpClient httpClient = RxHttpClient.create(new URL("http://localhost:8080"));
        Flowable<HttpResponse<Map>> call = httpClient.exchange(
                POST("/login", new UsernamePasswordCredentials("KingKong", "secret")),
                Map.class
        );
        Map<String, String> flatJSON = call.blockingFirst().body();
        httpClient.close();

        String token = flatJSON.get("access_token");

        JWTCallCredentials callCredentials = new JWTCallCredentials(token);
        GreeterGrpc.GreeterBlockingStub blockingStub = GreeterGrpc.newBlockingStub(channel).withCallCredentials(callCredentials);

        //Create Request
        HelloRequest.Builder builder = HelloRequest.newBuilder();
        builder.setName("Authenticated, >NO_ROLE<, JWT");
        HelloRequest request = builder.build();

        try {
            System.out.println("Calling gRPC service (Authenticated, >NO_ROLE<, JWT): " + GreeterGrpc.getSayHelloRolesMethod().getFullMethodName());
            HelloReply response = blockingStub.sayHelloRoles(request);
            System.out.println("Response from gRPC service: " + response.getMessage());
        } catch (StatusRuntimeException e) {
            System.out.println("RPC failed: " + e.getStatus());
        }
    }

    private static void callSayHello_Authenticated_ROLE_ADMIN_JWT(ManagedChannel channel) throws Exception{
        RxHttpClient httpClient = RxHttpClient.create(new URL("http://localhost:8080"));
        Flowable<HttpResponse<Map>> call = httpClient.exchange(
                POST("/login", new UsernamePasswordCredentials("KingKong_ADMIN", "secret")),
                Map.class
        );
        Map<String, String> flatJSON = call.blockingFirst().body();
        httpClient.close();

        String token = flatJSON.get("access_token");

        JWTCallCredentials callCredentials = new JWTCallCredentials(token);
        GreeterGrpc.GreeterBlockingStub blockingStub = GreeterGrpc.newBlockingStub(channel).withCallCredentials(callCredentials);

        //Create Request
        HelloRequest.Builder builder = HelloRequest.newBuilder();
        builder.setName("Authenticated, ROLE_ADMIN, JWT");
        HelloRequest request = builder.build();

        try {
            System.out.println("Calling gRPC service (Authenticated, ROLE_ADMIN, JWT): " + GreeterGrpc.getSayHelloRolesMethod().getFullMethodName());
            HelloReply response = blockingStub.sayHelloRoles(request);
            System.out.println("Response from gRPC service: " + response.getMessage());
        } catch (StatusRuntimeException e) {
            System.out.println("RPC failed: " + e.getStatus());
        }
    }

    private static void callSayHello_Unauthenticated_JWT(ManagedChannel channel) throws Exception{
        String token = "0815";

        JWTCallCredentials callCredentials = new JWTCallCredentials(token);
        GreeterGrpc.GreeterBlockingStub blockingStub = GreeterGrpc.newBlockingStub(channel).withCallCredentials(callCredentials);

        //Create Request
        HelloRequest.Builder builder = HelloRequest.newBuilder();
        builder.setName("Unauthenticated, JWT");
        HelloRequest request = builder.build();

        try {
            System.out.println("Calling gRPC service (Unauthenticated, JWT): " + GreeterGrpc.getSayHelloMethod().getFullMethodName());
            HelloReply response = blockingStub.sayHello(request);
            System.out.println("Response from gRPC service: " + response.getMessage());
        } catch (StatusRuntimeException e) {
            System.out.println("RPC failed: " + e.getStatus());
        }
    }

    private static void callSayHello_Authenticated_JWT(ManagedChannel channel) throws Exception{
        RxHttpClient httpClient = RxHttpClient.create(new URL("http://localhost:8080"));
        Flowable<HttpResponse<Map>> call = httpClient.exchange(
                POST("/login", new UsernamePasswordCredentials("KingKong", "secret")),
                Map.class
        );
        Map<String, String> flatJSON = call.blockingFirst().body();
        httpClient.close();

        String token = flatJSON.get("access_token");

        JWTCallCredentials callCredentials = new JWTCallCredentials(token);
        GreeterGrpc.GreeterBlockingStub blockingStub = GreeterGrpc.newBlockingStub(channel).withCallCredentials(callCredentials);

        //Create Request
        HelloRequest.Builder builder = HelloRequest.newBuilder();
        builder.setName("Authenticated, JWT");
        HelloRequest request = builder.build();

        try {
            System.out.println("Calling gRPC service (Authenticated, JWT): " + GreeterGrpc.getSayHelloMethod().getFullMethodName());
            HelloReply response = blockingStub.sayHello(request);
            System.out.println("Response from gRPC service: " + response.getMessage());
        } catch (StatusRuntimeException e) {
            System.out.println("RPC failed: " + e.getStatus());
        }
    }

    private static void callSayHelloRoles_Authenticated_NO_ROLE(ManagedChannel channel) {
        BasicAuthCallCredentials callCredentials = new BasicAuthCallCredentials("KingKong", "secret");
        GreeterGrpc.GreeterBlockingStub blockingStub = GreeterGrpc.newBlockingStub(channel).withCallCredentials(callCredentials);

        //Create Request
        HelloRequest.Builder builder = HelloRequest.newBuilder();
        builder.setName("Authenticated, >NO_ROLE<)");
        HelloRequest request = builder.build();

        try {
            System.out.println("Calling gRPC service (Authenticated, >NO_ROLE<): " + GreeterGrpc.getSayHelloRolesMethod().getFullMethodName());
            HelloReply response = blockingStub.sayHelloRoles(request);
            System.out.println("Response from gRPC service: " + response.getMessage());
        } catch (StatusRuntimeException e) {
            System.out.println("RPC failed: " + e.getStatus());
        }
    }

    private static void callSayHelloRoles_Authenticated_ROLE_ADMIN(ManagedChannel channel) {
        BasicAuthCallCredentials callCredentials = new BasicAuthCallCredentials("KingKong_ADMIN", "secret");
        GreeterGrpc.GreeterBlockingStub blockingStub = GreeterGrpc.newBlockingStub(channel).withCallCredentials(callCredentials);

        //Create Request
        HelloRequest.Builder builder = HelloRequest.newBuilder();
        builder.setName("Authenticated, ROLE_ADMIN");
        HelloRequest request = builder.build();

        try {
            System.out.println("Calling gRPC service (Authenticated, ROLE_ADMIN): " + GreeterGrpc.getSayHelloRolesMethod().getFullMethodName());
            HelloReply response = blockingStub.sayHelloRoles(request);
            System.out.println("Response from gRPC service: " + response.getMessage());
        } catch (StatusRuntimeException e) {
            System.out.println("RPC failed: " + e.getStatus());
        }
    }

    private static void callSayHelloAnonymous_Authenticated(ManagedChannel channel) {
        BasicAuthCallCredentials callCredentials = new BasicAuthCallCredentials("KingKong", "secret");
        GreeterGrpc.GreeterBlockingStub blockingStub = GreeterGrpc.newBlockingStub(channel).withCallCredentials(callCredentials);

        //Create Request
        HelloRequest.Builder builder = HelloRequest.newBuilder();
        builder.setName("Authenticated");
        HelloRequest request = builder.build();

        try {
            System.out.println("Calling gRPC service (Authenticated): " + GreeterGrpc.getSayHelloAnonymousMethod().getFullMethodName());
            HelloReply response = blockingStub.sayHelloAnonymous(request);
            System.out.println("Response from gRPC service: " + response.getMessage());
        } catch (StatusRuntimeException e) {
            System.out.println("RPC failed: " + e.getStatus());
        }
    }

    private static void callSayHelloAnonymous_Unauthenticated(ManagedChannel channel) {
        GreeterGrpc.GreeterBlockingStub blockingStub = GreeterGrpc.newBlockingStub(channel);

        //Create Request
        HelloRequest.Builder builder = HelloRequest.newBuilder();
        builder.setName("Unauthenticated");
        HelloRequest request = builder.build();

        try {
            System.out.println("Calling gRPC service (Unauthenticated): " + GreeterGrpc.getSayHelloAnonymousMethod().getFullMethodName());
            HelloReply response = blockingStub.sayHelloAnonymous(request);
            System.out.println("Response from gRPC service: " + response.getMessage());
        } catch (StatusRuntimeException e) {
            System.out.println("RPC failed: " + e.getStatus());
        }
    }

    private static void callSayHello_Authenticated(ManagedChannel channel) {
        BasicAuthCallCredentials callCredentials = new BasicAuthCallCredentials("KingKong", "secret");
        GreeterGrpc.GreeterBlockingStub blockingStub = GreeterGrpc.newBlockingStub(channel).withCallCredentials(callCredentials);

        //Create Request
        HelloRequest.Builder builder = HelloRequest.newBuilder();
        builder.setName("Authenticated");
        HelloRequest request = builder.build();

        try {
            System.out.println("Calling gRPC service (Authenticated): " + GreeterGrpc.getSayHelloMethod().getFullMethodName());
            HelloReply response = blockingStub.sayHello(request);
            System.out.println("Response from gRPC service: " + response.getMessage());
        } catch (StatusRuntimeException e) {
            System.out.println("RPC failed: " + e.getStatus());
        }
    }

    private static void callSayHello_Unauthenticated(ManagedChannel channel) {
        GreeterGrpc.GreeterBlockingStub blockingStub = GreeterGrpc.newBlockingStub(channel);

        //Create Request
        HelloRequest.Builder builder = HelloRequest.newBuilder();
        builder.setName("Unauthenticated");
        HelloRequest request = builder.build();

        try {
            System.out.println("Calling gRPC service (Unauthenticated): " + GreeterGrpc.getSayHelloMethod().getFullMethodName());
            HelloReply response = blockingStub.sayHello(request);
            System.out.println("Response from gRPC service: " + response.getMessage());
        } catch (StatusRuntimeException e) {
            System.out.println("RPC failed: " + e.getStatus());
        }
    }
}

class JWTCallCredentials extends CallCredentials2 {
    private String token;

    public JWTCallCredentials(String token) {
        this.token = token;
    }

    @Override
    public void applyRequestMetadata(RequestInfo requestInfo, Executor appExecutor, MetadataApplier applier) {
        appExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Metadata headers = new Metadata();
                    Metadata.Key<String> key = Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER);
                    String value = "Bearer " + token;
                    headers.put(key, value);
                    applier.apply(headers);
                } catch (Throwable e) {
                    applier.fail(Status.UNAUTHENTICATED.withCause(e));
                }
            }
        });
    }

    @Override
    public void thisUsesUnstableApi() {
    }
}


class BasicAuthCallCredentials extends CallCredentials2 {
    private String username;
    private String password;

    public BasicAuthCallCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public void applyRequestMetadata(RequestInfo requestInfo, Executor appExecutor, MetadataApplier applier) {
        appExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Metadata headers = new Metadata();
                    Metadata.Key<String> key = Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER);
                    String value = Base64.getEncoder().encodeToString((username + ":" + password).getBytes(UTF_8));
                    value = "Basic " + value;
                    headers.put(key, value);
                    applier.apply(headers);
                } catch (Throwable e) {
                    applier.fail(Status.UNAUTHENTICATED.withCause(e));
                }
            }
        });
    }

    @Override
    public void thisUsesUnstableApi() {
    }
}
