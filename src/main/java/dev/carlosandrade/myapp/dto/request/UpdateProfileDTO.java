package dev.carlosandrade.myapp.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class UpdateProfileDTO {
    private String name;
    private String email;
}
