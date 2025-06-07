package com.julianoclsantos.walletassignmentservice.mock;

import com.github.javafaker.Faker;
import com.julianoclsantos.walletassignmentservice.application.dto.WalletBalanceDTO;
import com.julianoclsantos.walletassignmentservice.application.dto.WalletBalanceHistoryDTO;
import com.julianoclsantos.walletassignmentservice.application.dto.WalletDTO;
import com.julianoclsantos.walletassignmentservice.infrastructure.web.controller.request.WalletDepositRequest;
import com.julianoclsantos.walletassignmentservice.infrastructure.web.controller.request.WalletRequest;
import com.julianoclsantos.walletassignmentservice.infrastructure.web.controller.request.WalletTransferRequest;
import com.julianoclsantos.walletassignmentservice.infrastructure.web.controller.request.WalletWithdrawRequest;
import org.assertj.core.util.Streams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class WalletControllerMock {

    private static final Faker faker = Faker.instance();

    public static final WalletRequest walletRequest = WalletRequest.builder()
            .name(faker.company().name())
            .userName(faker.name().fullName())
            .build();

    public static final WalletDepositRequest walletDepositRequest = WalletDepositRequest.builder()
            .walletCode(UUID.randomUUID().toString())
            .amount(BigDecimal.valueOf(faker.number().randomDouble(2, 1, 99999)))
            .build();

    public static final WalletTransferRequest walletTransferRequest = WalletTransferRequest.builder()
            .originWalletCode(UUID.randomUUID().toString())
            .destinationWalletCode(UUID.randomUUID().toString())
            .amount(BigDecimal.valueOf(faker.number().randomDouble(2, 1, 99999)))
            .build();

    public static final WalletWithdrawRequest walletWithdrawRequest = WalletWithdrawRequest.builder()
            .walletCode(UUID.randomUUID().toString())
            .amount(BigDecimal.valueOf(faker.number().randomDouble(2, 1, 99999)))
            .build();

    public static final WalletBalanceHistoryDTO walletBalanceHistoryDTO = WalletBalanceHistoryDTO.builder()
            .walletName(faker.company().name())
            .userName(faker.name().fullName())
            .totalAmount(BigDecimal.valueOf(faker.number().randomDouble(2, 1, 99999)))
            .build();

    public static final WalletBalanceDTO walletBalanceDTO = WalletBalanceDTO.builder()
            .walletName(faker.company().name())
            .userName(faker.name().fullName())
            .totalAmount(BigDecimal.valueOf(faker.number().randomDouble(2, 1, 99999)))
            .build();

    public static final List<WalletDTO> walletDTOList = Stream.generate(() ->
                    WalletDTO.builder()
                            .userName(faker.name().fullName())
                            .walletName(faker.company().name())
                            .walletCode(UUID.randomUUID().toString())
                            .build()
            )
            .limit(20)
            .toList();

    public static Page<WalletDTO> walletDTOPage = new PageImpl<>(walletDTOList, PageRequest.of(0, 10), walletDTOList.size());
}
