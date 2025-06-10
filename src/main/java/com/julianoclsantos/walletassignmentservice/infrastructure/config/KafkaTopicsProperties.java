package com.julianoclsantos.walletassignmentservice.infrastructure.config;

import com.julianoclsantos.walletassignmentservice.domain.enums.TopicsEnum;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.kafka")
public class KafkaTopicsProperties {

    private Consumer consumer;

    private Producer producer;

    private Map<String, String> topics;

    @Data
    public static class Consumer {
        private String groupId;
    }

    @Data
    public static class Producer {
        private String groupId;
    }

    public String getTopic(TopicsEnum key) {
        return topics.get(key.name());
    }
}