package com.julianoclsantos.walletassignmentservice.infrastructure.config;

import com.julianoclsantos.walletassignmentservice.domain.enums.TopicsEnum;
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

    private Map<String, String> topics;

    public String getTopic(TopicsEnum key) {
        return topics.get(key.name());
    }
}