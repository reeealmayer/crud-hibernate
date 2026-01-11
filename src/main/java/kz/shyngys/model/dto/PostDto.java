package kz.shyngys.model.dto;

import kz.shyngys.domain.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDto {
    private Long id;
    private String content;
    private LocalDateTime created;
    private LocalDateTime updated;
    private Status status;
    private Long writerId;
    private Set<LabelDto> labels;
}