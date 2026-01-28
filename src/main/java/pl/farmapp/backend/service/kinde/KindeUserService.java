package pl.farmapp.backend.service.kinde;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.farmapp.backend.config.KindeConfig;

import org.springframework.http.HttpHeaders;


@Service
@RequiredArgsConstructor
public class KindeUserService {

    private final RestTemplate restTemplate;
    private final KindeAuthService authService;
    private final KindeConfig config;

    private static final String ORG_CODE = "org_8a5f98134ab";


    public KindeUserService(RestTemplate restTemplate, KindeAuthService authService, KindeConfig config) {
        this.restTemplate = restTemplate;
        this.authService = authService;
        this.config = config;
    }

    public void deleteUser(String externalUserId) {

        String token = authService.getManagementToken();

        String url = "https://" + config.getDomain()
                + "/api/v1/organizations/"
                + ORG_CODE
                + "/users/"
                + externalUserId;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Void> response =
                restTemplate.exchange(url, HttpMethod.DELETE, request, Void.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to delete user in Kinde");
        }
    }
}
