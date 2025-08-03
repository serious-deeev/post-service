package org.serious.dev.service.impl;

import lombok.RequiredArgsConstructor;
import org.serious.dev.entity.Post;
import org.serious.dev.exception.NoSuchPostException;
import org.serious.dev.exception.PostAlreadyReservedException;
import org.serious.dev.exception.PostCancelException;
import org.serious.dev.exception.PostReservationException;
import org.serious.dev.grpc.PostResponse;
import org.serious.dev.mapper.PostMapper;
import org.serious.dev.repository.PostRepository;
import org.serious.dev.service.PostService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @Override
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NoSuchPostException(id));

        return postMapper.postToPostResponse(post);
    }

    @Override
    @Transactional
    public void reservePost(Long postId, Long userId) {
        Post post = postRepository.findByIdForUpdate(postId)
                .orElseThrow(() -> new NoSuchPostException(postId));

        if (post.isReserved()) {
            throw new PostAlreadyReservedException(postId, userId);
        }

        try {
            post.setReserved(true);
            postRepository.save(post);
        } catch (Exception e) {
            throw new PostReservationException(post.getId(), post.getUserId(), e);
        }
    }

    @Override
    @Transactional
    public void cancelPostReservation(Long postId, Long userId) {
        Post post = postRepository.findByIdForUpdate(postId)
                .orElseThrow(() -> new NoSuchPostException(postId));

        if (!post.isReserved()) {
            return;
        }

        try {
            post.setReserved(false);
            postRepository.save(post);
        } catch (Exception e) {
            throw new PostCancelException(post.getId(), post.getUserId(), e);
        }
    }
}
