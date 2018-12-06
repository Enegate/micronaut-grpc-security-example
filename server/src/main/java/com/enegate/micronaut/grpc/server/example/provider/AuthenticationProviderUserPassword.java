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

package com.enegate.micronaut.grpc.server.example.provider;

import io.micronaut.security.authentication.*;
import io.reactivex.Flowable;
import org.reactivestreams.Publisher;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Steve Schneider
 */

@Singleton
public class AuthenticationProviderUserPassword implements AuthenticationProvider {

    @Override
    public Publisher<AuthenticationResponse> authenticate(AuthenticationRequest authenticationRequest) {
        if (authenticationRequest.getIdentity().equals("KingKong") && authenticationRequest.getSecret().equals("secret")) {
            return Flowable.just(new UserDetails((String) authenticationRequest.getIdentity(), new ArrayList<>()));
        }
        if (authenticationRequest.getIdentity().equals("KingKong_ADMIN") && authenticationRequest.getSecret().equals("secret")) {
            return Flowable.just(new UserDetails((String) authenticationRequest.getIdentity(), Arrays.asList("ROLE_ADMIN")));
        }
        return Flowable.just(new AuthenticationFailed());
    }
}
