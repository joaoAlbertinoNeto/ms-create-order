package br.com.order.ms_order.infrastructure.adapters.in.rest;

import br.com.order.ms_order.domain.dto.OrderDTO;
import br.com.order.ms_order.infrastructure.adapters.out.bd.dto.OrderEntityDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
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

@SpringBootTest
@AutoConfigureWebMvc
class OrderControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderController orderController;

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

        mockMvc.perform(post("/order/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateOrderWithAdminRole_ShouldReturnOk() throws Exception {
        OrderDTO orderDTO = createTestOrderDTO();

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
} 