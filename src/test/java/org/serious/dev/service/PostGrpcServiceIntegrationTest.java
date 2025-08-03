package org.serious.dev.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.serious.dev.PostServiceApplication;
import org.serious.dev.TestGrpcClient;
import org.serious.dev.TestGrpcServerRunner;
import org.serious.dev.grpc.PostRequest;
import org.serious.dev.grpc.PostResponse;
import org.serious.dev.grpc.PostServiceGrpc;
import org.serious.dev.grpc.config.TestGrpcConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.jdbc.Sql;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(classes = {TestGrpcConfig.class, PostServiceApplication.class})
class PostGrpcServiceIntegrationTest {

    private final TestGrpcServerRunner testServerRunner;
    private final TestGrpcClient testGrpcClient;

    @AfterAll
    void shutdownNowServer() {
        testServerRunner.destroyNow();
    }

    @Test
    @Sql(
            scripts = "/test-scripts/insert-for-post-grpc-service-integration-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    void shouldRunTestGrpcRequest() throws IOException {
        testServerRunner.start();

        PostServiceGrpc.PostServiceBlockingStub stubClient = testGrpcClient.createStubClient();
        PostResponse actualUserResponse = stubClient.getPost(PostRequest.newBuilder()
                .setId(4)
                .build());

        assertThat(actualUserResponse).isNotNull();
        assertThat(actualUserResponse.getId()).isEqualTo(4);
    }
}
