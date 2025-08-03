package org.serious.dev.service.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.serious.dev.PostServiceApplication;
import org.serious.dev.grpc.PostResponse;
import org.serious.dev.service.PostService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@RequiredArgsConstructor
@SpringBootTest(classes = PostServiceApplication.class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class PostServiceIntegrationTest {

    private final PostService testPostService;

    @Test
    @Sql(
            scripts = "/test-scripts/insert-for-post-service-integration-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    void shouldGetUserById() {
        PostResponse actualUserResponse = testPostService.getPost(2L);

        assertThat(actualUserResponse).isNotNull();
        assertThat(actualUserResponse.getId()).isEqualTo(2L);
    }
}
