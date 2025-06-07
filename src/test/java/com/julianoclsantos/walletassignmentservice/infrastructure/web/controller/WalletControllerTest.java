package com.julianoclsantos.walletassignmentservice.infrastructure.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.julianoclsantos.walletassignmentservice.application.port.in.WalletService;
import com.julianoclsantos.walletassignmentservice.mock.WalletControllerMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class WalletControllerTest {

    private final WalletService service = mock(WalletService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Faker faker = Faker.instance();

    @InjectMocks
    private WalletController controller;

    @Test
    void shouldCreateWithSuccess() throws Exception {
        var request = WalletControllerMock.walletRequest;

        doNothing().when(service).create(request);

        var mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        mockMvc.perform(post("/wallet")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated());
    }

    @Test
    void shouldDepositWithSuccess() throws Exception {
        var request = WalletControllerMock.walletDepositRequest;

        doNothing().when(service).deposit(request);

        var mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        mockMvc.perform(post("/wallet/deposit")
                        .content(new ObjectMapper().writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated());
    }

    @Test
    void shouldWithdrawWithSuccess() throws Exception {
        var request = WalletControllerMock.walletWithdrawRequest;

        doNothing().when(service).withdraw(request);

        var mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        mockMvc.perform(post("/wallet/withdraw")
                        .content(new ObjectMapper().writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated());
    }

    @Test
    void shouldTransferWithSuccess() throws Exception {
        var request = WalletControllerMock.walletTransferRequest;

        doNothing().when(service).transfer(request);

        var mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        mockMvc.perform(post("/wallet/transfer")
                        .content(new ObjectMapper().writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated());
    }

    @ParameterizedTest
    @MethodSource("walletBalanceHistoryData")
    void shouldReturnBalanceHistoryWithSuccess(WalletBalanceHistory data) throws Exception {
        var response = WalletControllerMock.walletBalanceHistoryDTO;
        var walletCode = data.walletCode;
        String start = Objects.isNull(data.start) ? "" : data.start.toString();
        String end = Objects.isNull(data.end) ? "" : data.end.toString();

        when(service.balanceHistory(any(), any(), any())).thenReturn(response);

        var mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        mockMvc.perform(get("/wallet/balanceHistory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("walletCode", walletCode)
                        .param("createAtStart", start)
                        .param("createAtEnd", end)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletName").exists())
                .andExpect(jsonPath("$.userName").exists())
                .andExpect(jsonPath("$.totalAmount").exists());
    }

    @Test
    void shouldReturnBalanceWithSuccess() throws Exception {
        var response = WalletControllerMock.walletBalanceDTO;
        var walletCode = UUID.randomUUID().toString();

        when(service.getBalance(walletCode)).thenReturn(response);

        var mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        mockMvc.perform(get("/wallet/" + walletCode)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletName").exists())
                .andExpect(jsonPath("$.userName").exists())
                .andExpect(jsonPath("$.totalAmount").exists());
    }

    @ParameterizedTest
    @MethodSource("walletListData")
    void shouldReturnListWalletWithSuccess(WalletList data) throws Exception {
        var response = WalletControllerMock.walletDTOPage;
        var userName = data.userName;
        String start = Objects.isNull(data.start) ? "" : data.start.toString();
        String end = Objects.isNull(data.end) ? "" : data.end.toString();

        when(service.list(any(), any(), any(), any())).thenReturn(response);

        var mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();

        mockMvc.perform(get("/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("userName", userName)
                        .param("createAtStart", start)
                        .param("createAtEnd", end)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].walletName").exists())
                .andExpect(jsonPath("$.content[0].userName").exists())
                .andExpect(jsonPath("$.content[0].walletCode").exists());
    }

    private static List<WalletBalanceHistory> walletBalanceHistoryData() {
        return List.of(
                new WalletBalanceHistory(UUID.randomUUID().toString(), null, null),
                new WalletBalanceHistory(UUID.randomUUID().toString(), LocalDate.now().minusMonths(1), LocalDate.now())
        );
    }

    record WalletBalanceHistory(String walletCode, LocalDate start, LocalDate end) {
    }

    private static List<WalletList> walletListData() {
        return List.of(
                new WalletList(faker.name().fullName(), null, null),
                new WalletList(faker.name().fullName(), LocalDate.now().minusMonths(1), LocalDate.now())
        );
    }

    record WalletList(String userName, LocalDate start, LocalDate end) {
    }
}