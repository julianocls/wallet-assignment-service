package com.julianoclsantos.walletassignmentservice.infrastructure.adapter.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;


@Slf4j
@Service
@RequiredArgsConstructor
public class WalletEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void send(String topic, String key, Object payload) {
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, key, payload);

        future.thenAccept(result -> {
            RecordMetadata metadata = result.getRecordMetadata();
            log.info("Message sent successfully. Topic {} - partition: {}, offset: {}", metadata.topic(), metadata.partition(), metadata.offset());
        }).exceptionally(ex -> {
            log.error("Error sending message. Topic {}: {}", topic, ex.getMessage(), ex);

            return null;
        });
    }
}
