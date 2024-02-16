/**
 * 
 */
package com.dcsg.demo.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.dcsg.pc.eventing.schema.sku.SkuAttribute;

import lombok.extern.slf4j.Slf4j;
/**
 * 
 */
@Component
@Slf4j
public class TestConsumer {
	
	@KafkaListener(topics = "${kafka.topic.sku-delta}"  , groupId = "${kafka.relationship.group.id}",concurrency = "${kafka.listener.concurrency.relationshipconsumer}")
	public void consumeSkuRelationship(ConsumerRecord<String, SkuAttribute> deltaMsg,
			Acknowledgment acknowledgment) {
		
		log.info("Delta message consumed from sku delta topic in relationship consumer");
		log.info("Message is {}", deltaMsg);
		
		SkuAttribute deltaMessage = deltaMsg.value();
		acknowledgment.acknowledge();
	}	
	

	
}
