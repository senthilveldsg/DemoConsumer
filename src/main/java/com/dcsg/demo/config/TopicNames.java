package com.dcsg.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "kafka.topic")
@Data
public class TopicNames {

    private String ecodeDelta;
    private String styleDelta;
    private String skuDelta;
    private String entityLink;
    private String syndigoCopy;
    private String digitalAsset;
    private String deadLetter;
}

