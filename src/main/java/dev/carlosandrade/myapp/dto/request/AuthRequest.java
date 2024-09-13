package dev.carlosandrade.myapp.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class AuthRequest {
    private String username;
    private String password;
}
