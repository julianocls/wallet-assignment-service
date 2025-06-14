package com.julianoclsantos.walletassignmentservice.infrastructure.adapter.kafka.producer;

import com.julianoclsantos.walletassignmentservice.domain.enums.TopicsEnum;
import com.julianoclsantos.walletassignmentservice.infrastructure.config.KafkaTopicsProperties;
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
    private final KafkaTopicsProperties kafkaTopicsProperties;

    public void send(TopicsEnum topicEnum, String key, Object payload) {
        var topic = getTopic(topicEnum);

        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, key, payload);
        future.thenAccept(result -> {
            RecordMetadata metadata = result.getRecordMetadata();
            log.info("Message sent successfully. Topic {} - partition: {}, offset: {}", metadata.topic(), metadata.partition(), metadata.offset());
        }).exceptionally(ex -> {
            log.error("Error sending message. Topic {}: {}, error={}", topic, ex.getMessage(), ex.getMessage());

            return null;
        });
    }

    private String getTopic(TopicsEnum topic) {
        return kafkaTopicsProperties.getTopic(topic);
    }

}
