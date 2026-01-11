package kz.shyngys.utils;

import kz.shyngys.domain.Label;
import kz.shyngys.domain.Post;
import kz.shyngys.domain.Status;
import kz.shyngys.domain.Writer;
import kz.shyngys.model.dto.LabelDto;
import kz.shyngys.model.dto.PostDto;
import kz.shyngys.model.dto.WriterFullDto;

import java.time.LocalDateTime;
import java.util.HashSet;

public class TestDataUtils {

    public static Label createLabel(Long id, String name) {
        Label label = new Label();
        label.setId(id);
        label.setName(name);
        return label;
    }

    public static LabelDto createLabelDto(Long id, String name) {
        LabelDto labelDto = new LabelDto();
        labelDto.setId(id);
        labelDto.setName(name);
        return labelDto;
    }

    public static Writer createWriter() {
        return Writer.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .build();
    }

    public static Post createPost() {
        return Post.builder()
                .id(1L)
                .content("Test content")
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .status(Status.ACTIVE)
                .writer(createWriter())
                .labels(new HashSet<>())
                .build();
    }

    public static PostDto createPostDto() {
        Post post = createPost();
        return PostDto.builder()
                .id(1L)
                .content("Test content")
                .created(post.getCreated())
                .updated(post.getUpdated())
                .status(Status.ACTIVE)
                .writerId(1L)
                .labels(new HashSet<>())
                .build();
    }

    public static WriterFullDto createWriterFullDto() {
        return WriterFullDto.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .build();
    }
}
