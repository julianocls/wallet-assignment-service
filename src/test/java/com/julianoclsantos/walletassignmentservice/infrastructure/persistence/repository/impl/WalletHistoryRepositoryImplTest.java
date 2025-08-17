package com.julianoclsantos.walletassignmentservice.infrastructure.persistence.repository.impl;

import com.github.javafaker.Faker;
import com.julianoclsantos.walletassignmentservice.domain.enums.OperationStatusEnum;
import com.julianoclsantos.walletassignmentservice.domain.enums.TransactionTypeEnum;
import com.julianoclsantos.walletassignmentservice.domain.model.Wallet;
import com.julianoclsantos.walletassignmentservice.domain.model.WalletHistory;
import com.julianoclsantos.walletassignmentservice.infrastructure.exception.InternalErrorException;
import com.julianoclsantos.walletassignmentservice.infrastructure.mapper.WalletHistoryMapper;
import com.julianoclsantos.walletassignmentservice.infrastructure.persistence.entity.WalletEntity;
import com.julianoclsantos.walletassignmentservice.infrastructure.persistence.entity.WalletHistoryEntity;
import com.julianoclsantos.walletassignmentservice.infrastructure.persistence.repository.WalletHistoryJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.julianoclsantos.walletassignmentservice.domain.enums.MessageEnum.GENERIC_ERROR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletHistoryRepositoryImplTest {

    private final WalletHistoryJpaRepository jpaRepository = mock(WalletHistoryJpaRepository.class);
    private final WalletHistoryMapper mapper = mock(WalletHistoryMapper.class);

    @InjectMocks
    WalletHistoryRepositoryImpl walletHistoryRepository;

    @Test
    void shouldSaveWithSuccess() {
        var entity = getWalletHistoryEntity();

        walletHistoryRepository.save(entity);

        var entityCaptor = ArgumentCaptor.forClass(WalletHistoryEntity.class);
        verify(jpaRepository, times(1)).save(entityCaptor.capture());

        var savedEntity = entityCaptor.getValue();
        Assertions.assertNotNull(savedEntity);
        assertEquals(entity, savedEntity);
    }

    @Test
    void shouldThrowInternalErrorExceptionWhenSave() {
        var entity = getWalletHistoryEntity();
        var exception = new RuntimeException("Database error");

        when(jpaRepository.save(entity)).thenThrow(exception);

        var thrown = Assertions.assertThrows(InternalErrorException.class, () -> walletHistoryRepository.save(entity));
        assertEquals(GENERIC_ERROR.getMessage(), thrown.getMessage());
    }

    @Test
    void shouldUpdateOperationStatusWithSuccess() {
        var transactionCode = UUID.randomUUID().toString();
        var entity = getWalletHistoryEntity();
        var expected = getWalletHistory(entity);
        var response = Optional.of(entity);

        entity.setTransactionCode(transactionCode);

        when(jpaRepository.findByTransactionCode(transactionCode)).thenReturn(response);
        when(mapper.toDomain(entity)).thenReturn(expected);

        var actual = walletHistoryRepository.updateOperationStatus(transactionCode);

        assertNotNull(actual);
        assertEquals(Optional.of(expected), actual);
    }

    @Test
    void shouldThrowInternalErrorExceptionWhenUpdateOperationStatus() {
        var transactionCode = UUID.randomUUID().toString();
        var exception = new RuntimeException("Database error");

        when(jpaRepository.findByTransactionCode(transactionCode)).thenThrow(exception);

        var thrown = Assertions.assertThrows(InternalErrorException.class, () -> walletHistoryRepository.updateOperationStatus(transactionCode));
        assertEquals(GENERIC_ERROR.getMessage(), thrown.getMessage());
    }

    private static WalletHistoryEntity getWalletHistoryEntity() {
        Faker faker = Faker.instance();
        return WalletHistoryEntity.builder().id(faker.random().nextLong()).amount(BigDecimal.valueOf(faker.random().nextDouble())).wallet(WalletEntity.builder().build()).operationStatusEnum(OperationStatusEnum.CREATED).sourceWalletId(null).transactionCode(UUID.randomUUID().toString()).transactionType(TransactionTypeEnum.CREDIT).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
    }

    private WalletHistory getWalletHistory(WalletHistoryEntity entity) {
        return WalletHistory.builder().id(entity.getId()).amount(entity.getAmount()).wallet(Wallet.builder().build()).operationStatus(entity.getOperationStatusEnum()).sourceWalletId(entity.getSourceWalletId()).transactionCode(entity.getTransactionCode()).transactionType(entity.getTransactionType()).createdAt(entity.getCreatedAt()).updatedAt(entity.getUpdatedAt()).build();
    }

}