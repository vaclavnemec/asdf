package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
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

    private String apiUrl;

    private RestTemplate restTemplate;

    public RestLoanService(@Value("${apiUrl}") String apiUrl, RestTemplateBuilder builder) {
        this.apiUrl = apiUrl;
        this.restTemplate = builder.build();
    }

    @Override
    public List<Loan> getRecentLoans(int millis) {
        // the API correctly answers to the UTC time zone, no need to specify different time zone
        Instant instant = Instant.now().minusMillis(millis);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl + "/loans/marketplace")
                .queryParam("datePublished__gt", instant);

        log.info("Requesting loans from marketplace: {}", builder.toUriString());

        ResponseEntity<List<Loan>> response = restTemplate.exchange(builder.toUriString(),
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Loan>>() {});

        return response.getBody();
    }
}
