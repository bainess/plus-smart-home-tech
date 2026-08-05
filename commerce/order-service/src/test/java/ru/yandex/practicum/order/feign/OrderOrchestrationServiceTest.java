package ru.yandex.practicum.order.feign;

import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.order.dto.CreateOrderRequest;
import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.order.dto.OrderItemRequest;
import ru.yandex.practicum.order.exception.OrderProcessingException;
import ru.yandex.practicum.order.service.OrderService;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderOrchestrationServiceTest {

    @Mock
    private OrderService orderService;

    @Mock
    private ProductClient productClient;

    @Mock
    private InventoryClient inventoryClient;

    @InjectMocks
    private OrderOrchestrationService orchestrationService;

    private CreateOrderRequest request;

    @BeforeEach
    void setUp() {
        request = new CreateOrderRequest(
                "Ivan",
                "ivan@test.ru",
                List.of(
                        new OrderItemRequest(
                                1L,
                                "Lamp",
                                2,
                                new BigDecimal("100")
                        )
                )
        );
    }

    @Test
    void shouldCreateConfirmedOrder() {

        ProductDto product = new ProductDto(
                1L,
                "Lamp",
                "desc",
                new BigDecimal("100"),
                true
        );

        ReserveResponse reserve =
                new ReserveResponse(true, 10, "reserved");

        OrderDto order = mock(OrderDto.class);

        when(productClient.getProductById(1L))
                .thenReturn(product);

        when(inventoryClient.reserveStock(any()))
                .thenReturn(reserve);

        when(orderService.createOrder(request))
                .thenReturn(order);

        OrderDto result = orchestrationService.createOrder(request);

        assertThat(result).isEqualTo(order);

        verify(productClient).getProductById(1L);
        verify(inventoryClient).reserveStock(any());
        verify(orderService).createOrder(request);
        verify(inventoryClient, never()).releaseStock(any());
    }

    @Test
    void shouldMergeDuplicateProducts() {

        CreateOrderRequest duplicated = new CreateOrderRequest(
                "Ivan",
                "ivan@test.ru",
                List.of(
                        new OrderItemRequest(1L, "Lamp", 2, BigDecimal.TEN),
                        new OrderItemRequest(1L, "Lamp", 3, BigDecimal.TEN)
                )
        );

        when(productClient.getProductById(1L))
                .thenReturn(new ProductDto(
                        1L,
                        "Lamp",
                        "",
                        BigDecimal.TEN,
                        true
                ));

        when(inventoryClient.reserveStock(any()))
                .thenReturn(new ReserveResponse(true, 5, "ok"));

        when(orderService.createOrder(any()))
                .thenReturn(mock(OrderDto.class));

        orchestrationService.createOrder(duplicated);

        verify(productClient, times(1))
                .getProductById(1L);

        ArgumentCaptor<ReserveRequest> captor =
                ArgumentCaptor.forClass(ReserveRequest.class);

        verify(inventoryClient)
                .reserveStock(captor.capture());

        assertThat(captor.getValue().quantity())
                .isEqualTo(5);
    }

    @Test
    void shouldFailWhenProductInactive() {

        when(productClient.getProductById(1L))
                .thenReturn(new ProductDto(
                        1L,
                        "Lamp",
                        "",
                        BigDecimal.TEN,
                        false
                ));

        assertThatThrownBy(() -> orchestrationService.createOrder(request))
                .isInstanceOf(OrderProcessingException.class)
                .hasMessage("Product cannot be purchased");

        verify(inventoryClient, never())
                .reserveStock(any());

        verify(orderService, never())
                .createOrder(any());
    }

    @Test
    void shouldNotSaveOrderWhenReservationFails() {

        when(productClient.getProductById(1L))
                .thenReturn(new ProductDto(
                        1L,
                        "Lamp",
                        "",
                        BigDecimal.TEN,
                        true
                ));

        FeignException exception = mock(FeignException.class);
        when(exception.status()).thenReturn(409);

        when(inventoryClient.reserveStock(any()))
                .thenThrow(exception);

        assertThatThrownBy(() -> orchestrationService.createOrder(request))
                .isInstanceOf(OrderProcessingException.class)
                .hasMessageContaining("Недостаточно товара");

        verify(orderService, never())
                .createOrder(any());

        verify(inventoryClient, never())
                .releaseStock(any());
    }
}