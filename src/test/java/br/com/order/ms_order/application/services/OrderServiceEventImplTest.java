package br.com.order.ms_order.application.services;

import br.com.order.ms_order.application.ports.out.CreateOrderPortOut;
import br.com.order.ms_order.domain.dto.OrderCreatedDTO;
import br.com.order.ms_order.domain.dto.OrderDTO;
import lombok.SneakyThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceEventImplTest {

    private CreateOrderPortOut createOrderPortOut;
    private OrderServiceEventImpl orderServiceEventImpl;

    @BeforeEach
    void setUp() {
        createOrderPortOut = mock(CreateOrderPortOut.class);
        orderServiceEventImpl = new OrderServiceEventImpl(createOrderPortOut);
    }

    @SneakyThrows
    @Test
    void createOrder_success() {
        OrderDTO orderDTO = new OrderDTO();
        OrderCreatedDTO expected = new OrderCreatedDTO();
        when(createOrderPortOut.createOrder(any(OrderDTO.class))).thenReturn(expected);

        OrderCreatedDTO result = orderServiceEventImpl.createOrder(orderDTO);
        assertEquals(expected, result);
        verify(createOrderPortOut, times(1)).createOrder(orderDTO);
    }

    @SneakyThrows
    @Test
    void createOrder_exception() {
        OrderDTO orderDTO = new OrderDTO();
        when(createOrderPortOut.createOrder(any(OrderDTO.class))).thenThrow(new RuntimeException("Dependency error"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> orderServiceEventImpl.createOrder(orderDTO));
        assertTrue(thrown.getMessage().contains("Error creating order"));
        verify(createOrderPortOut, times(1)).createOrder(orderDTO);
    }
}
