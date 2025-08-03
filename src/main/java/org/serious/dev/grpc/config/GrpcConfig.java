package org.serious.dev.grpc.config;

import io.grpc.ServerInterceptor;
import org.serious.dev.GrpcServerRunner;
import org.serious.dev.service.PostGrpcService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Configuration
@Profile("prod")
public class GrpcConfig {

    @Bean
    public GrpcServerRunner GrpcServerRunner(
            @Value("${grpc.server.port}") Integer grpcServerPort,
            List<ServerInterceptor> interceptors,
            PostGrpcService postGrpcService
    ) {
        return new GrpcServerRunner(grpcServerPort, interceptors, postGrpcService);
    }
}
