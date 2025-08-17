package com.julianoclsantos.walletassignmentservice.infrastructure.persistence.repository.impl;

import com.julianoclsantos.walletassignmentservice.domain.model.Wallet;
import com.julianoclsantos.walletassignmentservice.infrastructure.exception.InternalErrorException;
import com.julianoclsantos.walletassignmentservice.infrastructure.mapper.WalletMapper;
import com.julianoclsantos.walletassignmentservice.infrastructure.persistence.entity.WalletEntity;
import com.julianoclsantos.walletassignmentservice.infrastructure.persistence.repository.WalletJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Optional;

import static com.julianoclsantos.walletassignmentservice.domain.enums.MessageEnum.GENERIC_ERROR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletRepositoryImplTest {

    private final WalletJpaRepository jpaRepository = mock(WalletJpaRepository.class);
    private final WalletMapper mapper = mock(WalletMapper.class);

    @InjectMocks
    private WalletRepositoryImpl walletRepository;

    @Test
    void shouldSaveWithSuccess() {
        var entity = WalletEntity.builder()
                .name("Test Wallet")
                .userName("testUser")
                .code("W123")
                .build();

        walletRepository.save(entity);

        ArgumentCaptor<WalletEntity> entityCaptor = ArgumentCaptor.forClass(WalletEntity.class);
        verify(jpaRepository).save(entityCaptor.capture());

        var savedEntity = entityCaptor.getValue();
        assertEquals("Test Wallet", savedEntity.getName());
        assertEquals("testUser", savedEntity.getUserName());
        assertEquals("W123", savedEntity.getCode());
    }

    @Test
    void shouldThrowInternalErrorExceptionWhenSave() {
        var entity = WalletEntity.builder()
                .name("Test Wallet")
                .userName("testUser")
                .code("W123")
                .build();

        var exception = new RuntimeException("Database error");
        when(jpaRepository.save(any(WalletEntity.class))).thenThrow(exception);

        var thrownException = assertThrows(InternalErrorException.class, () -> walletRepository.save(entity));
        assertEquals(GENERIC_ERROR.getMessage(), thrownException.getMessage());
    }


    @Test
    void shouldFindByUserNameAndWalletName() {
        var entity = WalletEntity.builder()
                .name("Test Wallet")
                .userName("testUser")
                .code("W123")
                .build();

        var wallet = Wallet.builder()
                .name("Test Wallet")
                .userName("testUser")
                .code("W123")
                .build();

        when(jpaRepository.findByUserNameAndName("testUser", "Test Wallet"))
                .thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(wallet);

        var result = walletRepository.findByUserNameAndWalletName("testUser", "Test Wallet");

        assertEquals(Optional.of(wallet), result);
    }

    @Test
    void shouldThrowInternalErrorExceptionWhenFindByUserNameAndWalletName() {
        var exception = new RuntimeException("Database error");
        when(jpaRepository.findByUserNameAndName("testUser", "Test Wallet"))
                .thenThrow(exception);

        var thrownException = assertThrows(InternalErrorException.class,
                () -> walletRepository.findByUserNameAndWalletName("testUser", "Test Wallet"));
        assertEquals(GENERIC_ERROR.getMessage(), thrownException.getMessage());
    }

    @Test
    void shouldFindByCode() {
        var entity = WalletEntity.builder()
                .name("Test Wallet")
                .userName("testUser")
                .code("W123")
                .build();

        var wallet = Wallet.builder()
                .name("Test Wallet")
                .userName("testUser")
                .code("W123")
                .build();

        when(jpaRepository.findByCode("W123")).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(wallet);

        var result = walletRepository.findByCode("W123");

        assertEquals(Optional.of(wallet), result);
    }

    @Test
    void shouldThrowInternalErrorExceptionWhenFindByCode() {
        var exception = new RuntimeException("Database error");
        when(jpaRepository.findByCode("W123")).thenThrow(exception);

        var thrownException = assertThrows(InternalErrorException.class,
                () -> walletRepository.findByCode("W123"));
        assertEquals(GENERIC_ERROR.getMessage(), thrownException.getMessage());
    }

    @Test
    void shouldSearchAllWithSuccess() {
        var pageable = mock(Pageable.class);
        var page = mock(Page.class);

        when(jpaRepository.searchAll("testUser", null, null, pageable))
                .thenReturn(page);
        when(page.map(any())).thenReturn(page);

        var result = walletRepository.searchAll("testUser", null, null, pageable);

        assertEquals(page, result);
    }

    @Test
    void shouldThrowInternalErrorExceptionWhenSearchAll() {
        var pageable = mock(Pageable.class);
        var exception = new RuntimeException("Database error");

        when(jpaRepository.searchAll("testUser", null, null, pageable))
                .thenThrow(exception);

        var thrownException = assertThrows(InternalErrorException.class,
                () -> walletRepository.searchAll("testUser", null, null, pageable));
        assertEquals(GENERIC_ERROR.getMessage(), thrownException.getMessage());
    }

    @Test
    void shouldGetBalanceHistoryWithSuccess() {
        var expectedBalance = new BigDecimal("100.00");
        when(jpaRepository.balanceHistory("W123", null, null))
                .thenReturn(expectedBalance);

        var result = walletRepository.balanceHistory("W123", null, null);

        assertEquals(expectedBalance, result);
        verify(jpaRepository).balanceHistory("W123", null, null);
    }

    @Test
    void shouldThrowInternalErrorExceptionWhenBalanceHistory() {
        var exception = new RuntimeException("Database error");
        when(jpaRepository.balanceHistory("W123", null, null))
                .thenThrow(exception);

        var thrownException = assertThrows(InternalErrorException.class,
                () -> walletRepository.balanceHistory("W123", null, null));
        assertEquals(GENERIC_ERROR.getMessage(), thrownException.getMessage());
    }

}