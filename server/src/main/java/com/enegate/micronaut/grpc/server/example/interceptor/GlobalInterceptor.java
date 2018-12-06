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

import com.enegate.micronaut.grpc.server.annotation.GrpcInterceptor;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.micronaut.core.order.Ordered;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Steve Schneider
 */

@GrpcInterceptor(global = true)
public class GlobalInterceptor implements ServerInterceptor, Ordered {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalInterceptor.class);

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
        LOG.info("gRPC service called: " + call.getMethodDescriptor().getFullMethodName());
        return next.startCall(call, headers);
    }

    @Override
    public int getOrder() {
        return 10;
    }
}
