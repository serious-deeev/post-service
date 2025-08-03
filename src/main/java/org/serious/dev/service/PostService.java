package org.serious.dev.service;

import org.serious.dev.grpc.PostResponse;

public interface PostService {

    PostResponse getPost(Long postId);

    void reservePost(Long postId, Long userId);

    void cancelPostReservation(Long postId, Long userId);
}
