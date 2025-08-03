package org.serious.dev.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.serious.dev.entity.Post;
import org.serious.dev.grpc.PostResponse;
import org.serious.dev.mapper.PostMapper;
import org.serious.dev.repository.PostRepository;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class PostServiceUnitTest {

    @Mock
    PostRepository fakePostRepository;

    @Mock
    PostMapper fakePostMapper;

    @InjectMocks
    PostServiceImpl postService;

    @Test
    void shouldGetUserById() {
        Post testPost = new Post(
                1L,
                2L,
                "Как много есть и не потолстеть",
                "Bla bla bla",
                false
        );

        doReturn(Optional.of(testPost))
                .when(fakePostRepository)
                .findById(1L);

        PostResponse expectedResponse = PostResponse.newBuilder()
                .setId(1L)
                .build();

        doReturn(expectedResponse)
                .when(fakePostMapper)
                .postToPostResponse(testPost);

        PostResponse actualResponse = postService.getPost(1L);

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }
}
