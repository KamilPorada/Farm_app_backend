package pl.farmapp.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KindeTokenDto {
    private String access_token;

    public String getAccessToken() {
        return access_token;
    }
}

