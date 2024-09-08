package com.jhohl.kitchensink;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HealthControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void healthCheck_ShouldReturnOK(){
        // Act: Call the health endpoint
        ResponseEntity<String> response = restTemplate.getForEntity("/health", String.class);

        // Assert: Verify the Response
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("OK");
    }
}
