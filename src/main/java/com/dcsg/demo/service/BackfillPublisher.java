package com.dcsg.demo.service;


import com.dcsg.pc.eventing.schema.sku.SkuAttribute;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;

@Service
@Slf4j
public class BackfillPublisher {

    private final WebClient webClient;
    private final String backfillUrl;

    private final HttpHeaders headers;

    @Autowired
    public BackfillPublisher(WebClient webClient, @Value("${backfill.post.url}") String backfillUrl) {
        this.webClient = webClient;
        this.backfillUrl = backfillUrl;
        headers = new HttpHeaders();
//        headers.setBasicAuth(authUser, authPassword);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
    }

    public void publish(SkuAttribute message) {

        String response = webClient.post()
            .uri(backfillUrl)
            .body(BodyInserters.fromValue(message))
            .headers(h -> h.addAll(headers))
            .retrieve()
            .onStatus(HttpStatusCode::isError, clientResponse -> null)
            .bodyToMono(String.class)
            .timeout(Duration.ofSeconds(5))
            .doOnSuccess(s -> log.info("Successfuly published to Backfill = " + message))
            .doOnError(e -> log.error("Unabel to publish to Backfill message =  {} and error is {}", message, e.getMessage()))
            .block();
        log.info(response);
    }

}
