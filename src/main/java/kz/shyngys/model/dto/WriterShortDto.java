package kz.shyngys.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WriterShortDto {
    private Long id;
    private String firstName;
    private String lastName;
}