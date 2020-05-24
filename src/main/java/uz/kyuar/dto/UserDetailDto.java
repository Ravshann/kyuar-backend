package uz.kyuar.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDetailDto {
    private String username;
    private String token;
}
