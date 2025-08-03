package org.serious.dev.service;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.serious.dev.grpc.PostRequest;
import org.serious.dev.grpc.PostResponse;
import org.serious.dev.grpc.PostServiceGrpc;
import org.serious.dev.grpc.ReservePostRequest;
import org.serious.dev.grpc.ReservePostResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostGrpcService extends PostServiceGrpc.PostServiceImplBase {

    private final PostService postService;

    @Override
    public void getPost(PostRequest request, StreamObserver<PostResponse> responseObserver) {
        PostResponse postResponse = postService.getPost(request.getId());

        responseObserver.onNext(postResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void reservePost(ReservePostRequest request, StreamObserver<ReservePostResponse> responseObserver) {
        postService.reservePost(request.getId(), request.getUserId());

        responseObserver.onNext(ReservePostResponse.newBuilder().build());
        responseObserver.onCompleted();
    }
}
