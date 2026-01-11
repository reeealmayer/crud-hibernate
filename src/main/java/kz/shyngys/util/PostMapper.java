package kz.shyngys.util;

import kz.shyngys.domain.Label;
import kz.shyngys.domain.Post;
import kz.shyngys.domain.Writer;
import kz.shyngys.model.dto.LabelDto;
import kz.shyngys.model.dto.PostDto;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class PostMapper {
    private PostMapper() {
    }

    public static Post toPost(PostDto dto) {
        Post post = new Post();
        post.setId(dto.getId());
        post.setContent(dto.getContent());

        if (dto.getCreated() != null) {
            post.setCreated(dto.getCreated());
        }

        if (dto.getUpdated() != null) {
            post.setUpdated(dto.getUpdated());
        }

        if (dto.getStatus() != null) {
            post.setStatus(dto.getStatus());
        }

        if (dto.getWriterId() != null) {
            Writer writer = new Writer();
            writer.setId(dto.getWriterId());
            post.setWriter(writer);
        }

        if (dto.getLabels() != null) {
            Set<Label> labels = dto.getLabels().stream()
                    .map(LabelMapper::toLabel)
                    .collect(Collectors.toSet());
            post.setLabels(labels);
        }

        return post;
    }

    public static PostDto toPostDto(Post post) {
        Set<LabelDto> labelDtos = Collections.emptySet();

        if (post.getLabels() != null) {
            labelDtos = post.getLabels().stream()
                    .map(LabelMapper::toLabelDto)
                    .collect(Collectors.toSet());
        }

        Long writerId = post.getWriter() != null ? post.getWriter().getId() : null;

        return new PostDto(
                post.getId(),
                post.getContent(),
                post.getCreated(),
                post.getUpdated(),
                post.getStatus(),
                writerId,
                labelDtos
        );
    }
}
