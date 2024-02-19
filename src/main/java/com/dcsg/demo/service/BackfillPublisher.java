package com.dcsg.demo.service;


import com.dcsg.pc.eventing.schema.sku.SkuAttribute;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Service
@Slf4j
public class BackfillPublisher {

    private final WebClient webClient;

    private final ObjectMapper objectMapper;
    private final String backfillUrl;

    private final HttpHeaders headers;

    @Autowired
    public BackfillPublisher(WebClient webClient, ObjectMapper objectMapper, @Value("${backfill.post.url}") String backfillUrl) {
        this.webClient = webClient;
        this.objectMapper = objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        this.backfillUrl = backfillUrl;
        headers = new HttpHeaders();
//        headers.setBasicAuth(authUser, authPassword);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN));
    }

    public void publish(SkuAttribute message) throws JsonProcessingException {
//        var msgStr = objectMapper.writeValueAsString(message);
//        BodyInserter<SkuAttribute, ReactiveHttpOutputMessage> body = BodyInserters.fromValue(message);
        String response = webClient.post()
            .uri(backfillUrl)
            .body(BodyInserters.fromValue(message.toString()))
            .headers(h -> h.addAll(headers))
            .retrieve()
            .bodyToMono(String.class)
            .timeout(Duration.ofSeconds(5))
            .doOnSuccess(s -> log.info("Successfuly published to Backfill = " + message))
            .doOnError(e -> log.error("Unable to publish to Backfill message =  {} and error is {}", message, e.getMessage()))
            .block();
        log.info(response);
    }

}
