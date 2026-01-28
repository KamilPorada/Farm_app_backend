package pl.farmapp.backend.service.kinde;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import pl.farmapp.backend.config.KindeConfig;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class KindeAuthService {

    private final RestTemplate restTemplate;
    private final KindeConfig config;

    public KindeAuthService(RestTemplate restTemplate, KindeConfig config) {
        this.restTemplate = restTemplate;
        this.config = config;
    }

    public String getManagementToken() {

        String url = "https://" + config.getDomain() + "/oauth2/token";


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("client_id", config.getM2m().getClientId());
        body.add("client_secret", config.getM2m().getClientSecret());
        body.add("audience", "https://" + config.getDomain() + "/api");
        body.add("scope", "delete:users");
        body.add("scope", "delete:organization_users");


        HttpEntity<MultiValueMap<String, String>> request =
                new HttpEntity<>(body, headers);

        ResponseEntity<Map> response =
                restTemplate.postForEntity(url, request, Map.class);

        if (!response.getStatusCode().is2xxSuccessful()
                || response.getBody() == null
                || response.getBody().get("access_token") == null) {
            throw new RuntimeException("Failed to obtain Kinde management token");
        }

        return response.getBody().get("access_token").toString();
    }
}
