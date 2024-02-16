/**
 *
 */
package com.dcsg.demo.service;

import com.dcsg.pc.eventing.schema.sku.SkuAttribute;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Component
@Slf4j
public class TestConsumer {

    private final BackfillPublisher backfillPublisher;


    @Autowired
    public TestConsumer(BackfillPublisher backfillPublisher) {
        this.backfillPublisher = backfillPublisher;
    }

    @KafkaListener(topics = "${kafka.topic.sku-delta}", groupId = "${kafka.relationship.group.id}", concurrency = "${kafka.listener.concurrency.relationshipconsumer}")
    public void consumeSkuRelationship(ConsumerRecord<String, SkuAttribute> deltaMsg,
                                       Acknowledgment acknowledgment) {
        SkuAttribute deltaMessage = deltaMsg.value();
        backfillPublisher.publish(deltaMessage);
        acknowledgment.acknowledge();
    }


}
