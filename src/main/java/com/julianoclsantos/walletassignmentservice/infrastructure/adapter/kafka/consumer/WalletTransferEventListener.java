package com.julianoclsantos.walletassignmentservice.infrastructure.adapter.kafka.consumer;

import com.julianoclsantos.walletassignmentservice.application.port.in.WalletHistoryService;
import com.julianoclsantos.walletassignmentservice.infrastructure.web.controller.request.WalletTransferEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WalletTransferEventListener {

    private final WalletHistoryService service;

    @KafkaListener(
            topics = "${spring.kafka.topics.WALLET_ASSIGNMENT_WALLET_TRANSFER}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleWalletTransfer(WalletTransferEvent event) throws InterruptedException {
        log.info(
                "Event received - WalletTransferEvent: {} to {}",
                event.getOriginTransactionCode(), event.getDestinationTransactionCode()
        );

        Thread.sleep(3000); // Delay simulation

        service.updateOperationStatus(event.getOriginTransactionCode());
        service.updateOperationStatus(event.getDestinationTransactionCode());

        log.info("Event received - WalletHistory updated!");

    }
}
