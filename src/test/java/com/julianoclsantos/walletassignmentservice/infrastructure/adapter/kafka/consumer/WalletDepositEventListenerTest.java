package com.julianoclsantos.walletassignmentservice.infrastructure.adapter.kafka.consumer;

import com.julianoclsantos.walletassignmentservice.application.port.in.WalletHistoryService;
import com.julianoclsantos.walletassignmentservice.infrastructure.web.controller.request.WalletDepositEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Container
    static ConfluentKafkaContainer kafka = new ConfluentKafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.6.0"));

    @MockitoBean
    private WalletHistoryService walletHistoryService;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    @Value("${spring.kafka.topics.WALLET_ASSIGNMENT_WALLET_DEPOSIT}")
    private String walletDepositTopic;

    @Value("${spring.kafka.topics.WALLET_ASSIGNMENT_WALLET_TRANSFER:wallet.assignment.service.wallet-transfer}")
    private String walletTransferTopic;
    @Value("${spring.kafka.topics.WALLET_ASSIGNMENT_WALLET_WITHDRAW:wallet.assignment.service.wallet-withdraw}")
    private String walletWithdrawTopic;


    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
        registry.add("spring.kafka.properties.schema.registry.url", () -> "mock://not-used");

        registry.add("spring.kafka.topics.WALLET_ASSIGNMENT_WALLET_DEPOSIT", () -> "wallet.assignment.service.wallet-deposit");
        registry.add("spring.kafka.topics.WALLET_ASSIGNMENT_WALLET_TRANSFER", () -> "wallet.assignment.service.wallet-transfer");
        registry.add("spring.kafka.topics.WALLET_ASSIGNMENT_WALLET_WITHDRAW", () -> "wallet.assignment.service.wallet-withdraw");
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
        WalletDepositEvent event = WalletDepositEvent.newBuilder()
                .setTransactionCode(transactionCode)
                .setWalletCode(walletCode)
                .setAmount(new BigDecimal("100.0"))
                .build();

        kafkaTemplate.send(walletDepositTopic, transactionCode, event);

        await().atMost(10, TimeUnit.SECONDS).untilAsserted(() -> {
            verify(walletHistoryService, times(1)).updateOperationStatus(transactionCode, walletCode);
        });
    }
}