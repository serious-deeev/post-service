package org.serious.dev;

import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import org.serious.dev.grpc.PostServiceGrpc;

@RequiredArgsConstructor
public class TestGrpcClient {

    private final String testGrpcServerUrl;
    private final Integer testGrpcServerPort;

    public PostServiceGrpc.PostServiceBlockingStub createStubClient() {
        return PostServiceGrpc.newBlockingStub(getChannel());
    }

    private Channel getChannel() {
        return ManagedChannelBuilder.forAddress(testGrpcServerUrl, testGrpcServerPort)
                .usePlaintext()
                .build();
    }
}
