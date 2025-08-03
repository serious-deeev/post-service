package org.serious.dev.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.serious.dev.entity.Post;
import org.serious.dev.grpc.PostResponse;

@Mapper(componentModel = "Spring")
public interface PostMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "post.id", target = "id")
    PostResponse postToPostResponse(Post post);
}
