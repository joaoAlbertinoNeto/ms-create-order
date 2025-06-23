package br.com.order.ms_order.application.services;

import br.com.order.ms_order.application.ports.out.CreateOrderPortOut;
import br.com.order.ms_order.domain.dto.OrderCreatedDTO;
import br.com.order.ms_order.domain.dto.OrderDTO;
import br.com.order.ms_order.infrastructure.adapters.out.bd.dto.OrderEntityDTO;
import br.com.order.ms_order.infrastructure.adapters.out.bd.repository.OrderRepository;
import org.modelmapper.ModelMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceEventImplTest {

    private CreateOrderPortOut createOrderPortOut;
    private OrderServiceEventImpl orderServiceEventImpl;
    private OrderRepository orderRepository;
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        createOrderPortOut = mock(CreateOrderPortOut.class);
        orderRepository = mock(OrderRepository.class);
        modelMapper = mock(ModelMapper.class);
        orderServiceEventImpl = new OrderServiceEventImpl(createOrderPortOut, orderRepository, modelMapper);
    }

    @Test
    void createOrder_success() throws JsonProcessingException {
        // Given
        OrderEntityDTO orderEntity = new OrderEntityDTO();
        orderEntity.setId("1");
        orderEntity.setCode("TEST001");
        orderEntity.setStatus("PENDING");
        
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setCode("TEST001");
        
        when(orderRepository.findByStatus("PENDING")).thenReturn(Arrays.asList(orderEntity));
        when(modelMapper.map(any(OrderEntityDTO.class), eq(OrderDTO.class))).thenReturn(orderDTO);
        when(orderRepository.save(any(OrderEntityDTO.class))).thenReturn(orderEntity);

        // When
        orderServiceEventImpl.createOrder();

        // Then
        verify(orderRepository, times(1)).findByStatus("PENDING");
        verify(modelMapper, times(1)).map(orderEntity, OrderDTO.class);
        verify(createOrderPortOut, times(1)).createOrder(orderDTO);
        verify(orderRepository, times(1)).save(any(OrderEntityDTO.class));
    }

    @Test
    void createOrder_noPendingOrders() throws JsonProcessingException {
        // Given
        when(orderRepository.findByStatus("PENDING")).thenReturn(Collections.emptyList());

        // When
        orderServiceEventImpl.createOrder();

        // Then
        verify(orderRepository, times(1)).findByStatus("PENDING");
        verify(createOrderPortOut, never()).createOrder(any(OrderDTO.class));
        verify(orderRepository, never()).save(any(OrderEntityDTO.class));
    }

    @Test
    void createOrder_exception() {
        // Given
        when(orderRepository.findByStatus("PENDING")).thenThrow(new RuntimeException("Database error"));

        // When & Then
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> orderServiceEventImpl.createOrder());
        assertTrue(thrown.getMessage().contains("Error creating order"));
        verify(orderRepository, times(1)).findByStatus("PENDING");
    }
}
