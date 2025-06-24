package br.com.order.ms_order.infrastructure.adapters.in.rest;

import br.com.order.ms_order.application.services.OrderService;
import br.com.order.ms_order.domain.dto.OrderCreatedDTO;
import br.com.order.ms_order.domain.dto.OrderDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

@WebMvcTest(OrderControllerImpl.class)
class OrderControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean(name = "OrderServiceImpl")
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String TOKEN = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJHZ1VYYlVtcUVRN2ZmXzNPMHZsSm54LWxCdjlLOW9ETTd4SktnQnA3RktBIn0.eyJleHAiOjE3NTA3MDM5MTgsImlhdCI6MTc1MDcwMzYxOCwianRpIjoib25ydHJvOmYzNzA2NzNmLWYxOWMtNDk2Yi1iYjE3LTkxYTFlZDg3NWI0ZCIsImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4MC9yZWFsbXMvbXMtb3JkZXIiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJtcy1vcmRlci1jbGllbnQiLCJzaWQiOiI5YjQ2Mzc5Mi05NWNkLTQ5NDMtOTk2OS0xYjdmYWVhNjQxMTQiLCJzY29wZSI6InByb2ZpbGUgZW1haWwiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsIm5hbWUiOiJUZXN0IFVzZXIiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ0ZXN0dXNlciIsImdpdmVuX25hbWUiOiJUZXN0IiwiZmFtaWx5X25hbWUiOiJVc2VyIiwiZW1haWwiOiJ0ZXN0QGV4YW1wbGUuY29tIn0.nhT7NhlItP8hL83GFq8Fm22Phqf86e9m4M2S4qKzoFCFU0VMQiQqLYi5xwVFSFRQZ1S4IYXcS2_AEXsiAbcRu4CNJk_0CHdxwN-O2Yi1Ac_VIyASInwWiLcC6izLkY-T5WqwF3Um0XNwnjhpM6-MasnR13nsI74OeNHPoHCGXkXnBJXvdpreumP0JAYKI86KXN4_6dvXIUvJdRKOPPw8AorLq6ZfgkTjvCj1OTJNNddG0gIyGcKWg8c_qaQBq8G8rqwgR-QQ6sPXqyOlkZGla7vnf5u5D8a06b4A6Q_lfN5xcSQBUN-X_j5OW5Yh3rI9DNx0QeUxwY3tmP-os2aXUg";

    @Test
    void testCreateOrderWithoutAuth_ShouldReturnUnauthorized() throws Exception {
        OrderDTO orderDTO = createTestOrderDTO();

        mockMvc.perform(post("/order/create")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization",TOKEN)
                .content(objectMapper.writeValueAsString(orderDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateOrderWithUserRole_ShouldReturnOk() throws Exception {
        OrderDTO orderDTO = createTestOrderDTO();
        OrderCreatedDTO createdOrder = createTestOrderCreatedDTO();
        
        when(orderService.createOrder(any(OrderDTO.class))).thenReturn(createdOrder);

        mockMvc.perform(post("/order/create")
                .contentType(MediaType.APPLICATION_JSON)
                .with(jwt().authorities(() -> "writter"))
                .content(objectMapper.writeValueAsString(orderDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testCreateOrderWithAdminRole_ShouldReturnOk() throws Exception {
        OrderDTO orderDTO = createTestOrderDTO();
        OrderCreatedDTO createdOrder = createTestOrderCreatedDTO();
        
        when(orderService.createOrder(any(OrderDTO.class))).thenReturn(createdOrder);

        mockMvc.perform(post("/order/create")
                .contentType(MediaType.APPLICATION_JSON)
                .with(jwt().authorities(() -> "writter"))
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