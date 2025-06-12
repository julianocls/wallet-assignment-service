package com.julianoclsantos.walletassignmentservice.infrastructure.adapter.kafka.consumer;

import com.julianoclsantos.walletassignmentservice.application.port.in.WalletHistoryService;
import com.julianoclsantos.walletassignmentservice.infrastructure.web.controller.request.WalletDepositEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.ConfluentKafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.*;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1)
@Testcontainers
class WalletDepositEventListenerTest {

    public static final String SCHEMA_REGISTRY = "mock://schema-registry";
    private static final String WALLET_ASSIGNMENT_WALLET_DEPOSIT = "wallet.assignment.service.wallet-deposit";

    @Container
    static ConfluentKafkaContainer kafka = new ConfluentKafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.6.0"));

    @MockitoBean
    private WalletHistoryService walletHistoryService;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
        registry.add("spring.kafka.properties.schema.registry.url", () -> SCHEMA_REGISTRY);
        registry.add("spring.kafka.topics.WALLET_ASSIGNMENT_WALLET_DEPOSIT", () -> "wallet.assignment.service.wallet-deposit");
    }

    @BeforeEach
    void setUp() {
        reset(walletHistoryService);

        kafkaListenerEndpointRegistry.getAllListenerContainers().forEach(container -> {
            if (container instanceof ConcurrentMessageListenerContainer) {
                ContainerTestUtils.waitForAssignment(container, ((ConcurrentMessageListenerContainer<?, ?>) container).getConcurrency());
            } else {
                ContainerTestUtils.waitForAssignment(container, 1);
            }
        });
    }

    @Test
    void shouldHandleWalletDepositEvent() {
        String transactionCode = UUID.randomUUID().toString();
        String walletCode = UUID.randomUUID().toString();
        WalletDepositEvent event = WalletDepositEvent.newBuilder().setTransactionCode(transactionCode).setWalletCode(walletCode).setAmount(new BigDecimal("100.0")).build();

        kafkaTemplate.send(WALLET_ASSIGNMENT_WALLET_DEPOSIT, transactionCode, event);

        await().atMost(10, TimeUnit.SECONDS).untilAsserted(() -> verify(walletHistoryService, times(1)).updateOperationStatus(transactionCode, walletCode));
    }
}