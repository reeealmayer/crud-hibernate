package kz.shyngys.util;

import kz.shyngys.domain.Label;
import kz.shyngys.model.dto.LabelDto;

public class LabelMapper {
    private LabelMapper() {
    }

    public static Label toLabel(LabelDto dto) {
        Label label = new Label();
        label.setId(dto.getId());
        label.setName(dto.getName());
        return label;
    }

    public static LabelDto toLabelDto(Label label) {
        return new LabelDto(
                label.getId(),
                label.getName()
        );
    }
}
