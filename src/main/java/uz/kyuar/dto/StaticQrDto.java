package uz.kyuar.dto;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.*;


@Data
public class StaticQrDto {
    @NotBlank
    @Size(min = 1, max = 1000)
    private String content;

    @Positive
    @NotNull
    private Integer type;

}
