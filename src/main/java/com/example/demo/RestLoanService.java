package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.util.List;

@Service
public class RestLoanService implements LoanService {

    private static final Logger log = LoggerFactory.getLogger(LoanTask.class);

    private final String apiUrl;

    private final RestTemplate restTemplate;

    public RestLoanService(@Value("${apiUrl}") String apiUrl, RestTemplateBuilder builder) {
        this.apiUrl = apiUrl;
        this.restTemplate = builder.build();
    }

    @Override
    public List<Loan> getRecentLoans(int millis) {

        if (millis <= 0) {
            throw new IllegalArgumentException("Parameter millis must be a positive number");
        }

        // the API correctly answers to the UTC time zone, no need to specify different time zone
        Instant instant = Instant.now().minusMillis(millis);

        // there are two possible problems in the next 10 lines of the code, lets discuss this during the interview
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl + "/loans/marketplace")
                .queryParam("datePublished__gt", instant);

        log.info("Requesting loans from marketplace: {}", builder.toUriString());

        final HttpHeaders headers = new HttpHeaders();
        headers.set("X-Size", "10000");
        HttpEntity entity = new HttpEntity(headers);

        ResponseEntity<List<Loan>> response = restTemplate.exchange(builder.toUriString(),
                HttpMethod.GET, entity, new ParameterizedTypeReference<List<Loan>>() {});

        return response.getBody();
    }
}
