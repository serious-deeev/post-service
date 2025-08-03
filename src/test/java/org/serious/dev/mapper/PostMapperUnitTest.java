package org.serious.dev.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.serious.dev.entity.Post;
import org.serious.dev.grpc.PostResponse;

import static org.assertj.core.api.Assertions.assertThat;

class PostMapperUnitTest {

    private PostMapper testPostMapper;

    @BeforeEach
    void initTestMapper() {
        testPostMapper = new PostMapperImpl();
    }

    @Test
    void shouldMapUserToUserResponseDto() {
        Post testPost = Post.builder()
                .id(1100L)
                .userId(2L)
                .title("Как много есть и не потолстеть")
                .content("Bla bla bla")
                .reserved(false)
                .build();

        PostResponse expectedUserResponse = PostResponse.newBuilder()
                .setId(1100L)
                .build();

        PostResponse actualUserResponse = testPostMapper.postToPostResponse(testPost);
        assertThat(actualUserResponse).isEqualTo(expectedUserResponse);
    }

    @Test
    void shouldReturnNullWhenInputIsNull() {
        assertThat(testPostMapper.postToPostResponse(null)).isNull();
    }
}
