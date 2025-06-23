package br.com.order.ms_order.infrastructure.adapters.in.rest;

import br.com.order.ms_order.application.services.OrderService;
import br.com.order.ms_order.domain.dto.OrderCreatedDTO;
import br.com.order.ms_order.domain.dto.OrderDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderControllerImpl.class)
class OrderControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateOrderWithoutAuth_ShouldReturnUnauthorized() throws Exception {
        OrderDTO orderDTO = createTestOrderDTO();

        mockMvc.perform(post("/order/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testCreateOrderWithUserRole_ShouldReturnOk() throws Exception {
        OrderDTO orderDTO = createTestOrderDTO();
        OrderCreatedDTO createdOrder = createTestOrderCreatedDTO();
        
        when(orderService.createOrder(any(OrderDTO.class))).thenReturn(createdOrder);

        mockMvc.perform(post("/order/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateOrderWithAdminRole_ShouldReturnOk() throws Exception {
        OrderDTO orderDTO = createTestOrderDTO();
        OrderCreatedDTO createdOrder = createTestOrderCreatedDTO();
        
        when(orderService.createOrder(any(OrderDTO.class))).thenReturn(createdOrder);

        mockMvc.perform(post("/order/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetOrderWithoutAuth_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/order/123"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetOrderWithUserRole_ShouldReturnOk() throws Exception {
        OrderCreatedDTO createdOrder = createTestOrderCreatedDTO();
        
        when(orderService.getByOrderId("123")).thenReturn(createdOrder);

        mockMvc.perform(get("/order/123"))
                .andExpect(status().isOk());
    }

    @Test
    void testHealthEndpoint_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk());
    }

    private OrderDTO createTestOrderDTO() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setCode("TEST001");
        orderDTO.setCustomerEmail("test@example.com");
        orderDTO.setCustomerTelephoneNumber("11999999999");
        orderDTO.setStatus("PENDING");
        orderDTO.setCreatedAt(LocalDateTime.now());
        return orderDTO;
    }

    private OrderCreatedDTO createTestOrderCreatedDTO() {
        OrderCreatedDTO createdOrder = new OrderCreatedDTO();
        createdOrder.setId("123");
        createdOrder.setCode("TEST001");
        createdOrder.setCustomerEmail("test@example.com");
        createdOrder.setCustomerTelephoneNumber("11999999999");
        createdOrder.setStatus("PENDING");
        createdOrder.setCreatedAt(LocalDateTime.now());
        createdOrder.setOrderLink("http://localhost:8081/order/123");
        return createdOrder;
    }
} 