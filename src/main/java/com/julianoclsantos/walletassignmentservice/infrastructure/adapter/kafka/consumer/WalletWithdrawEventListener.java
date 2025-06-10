package com.julianoclsantos.walletassignmentservice.infrastructure.adapter.kafka.consumer;

import com.julianoclsantos.walletassignmentservice.application.port.in.WalletHistoryService;
import com.julianoclsantos.walletassignmentservice.infrastructure.web.controller.request.WalletDepositEvent;
import com.julianoclsantos.walletassignmentservice.infrastructure.web.controller.request.WalletWithdrawEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WalletWithdrawEventListener {

    private final WalletHistoryService service;

    @KafkaListener(
            topics = "${spring.kafka.topics.WALLET_ASSIGNMENT_WALLET_WITHDRAW}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleWalletWithdraw(WalletWithdrawEvent event) {
        log.info("Event received - WalletWithdrawEvent: {}", event);

        service.updateOperationStatus(event.getTransactionCode(), event.getWalletCode());

        log.info("Event received - WalletHistory updated!");

    }
}
