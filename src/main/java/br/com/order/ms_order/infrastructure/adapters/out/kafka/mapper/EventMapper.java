package br.com.order.ms_order.infrastructure.adapters.out.kafka.mapper;

import br.com.order.ms_order.domain.dto.OrderCreatedDTO;
import br.com.order.ms_order.domain.dto.OrderDTO;
import br.com.order.ms_order.infrastructure.adapters.out.kafka.dto.OrderEventDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EventMapper {

    public OrderEventDTO mapToEvent(OrderDTO orderDTO){
        var orderEvent = new OrderEventDTO();
        orderEvent.setCode(orderDTO.getCode());
        orderEvent.setStatus(orderDTO.getStatus());
        orderEvent.setCreatedAt(orderDTO.getCreatedAt());
        orderEvent.setCustomerTelephoneNumber(orderDTO.getCustomerTelephoneNumber());
        orderEvent.setCustomerEmail(orderDTO.getCustomerEmail());
        orderEvent.setUuid(String.valueOf(UUID.randomUUID()));
        return orderEvent;
    }

    public String mapToStringEvent(OrderEventDTO orderDTO) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(orderDTO);
    }

    public OrderCreatedDTO mapFromEvent(OrderEventDTO orderEventDTO){
        var orderEvent = new OrderCreatedDTO();
        orderEvent.setCode(orderEventDTO.getCode());
        orderEvent.setStatus(orderEventDTO.getStatus());
        orderEvent.setCreatedAt(orderEventDTO.getCreatedAt());
        orderEvent.setCustomerTelephoneNumber(orderEventDTO.getCustomerTelephoneNumber());
        orderEvent.setCustomerEmail(orderEventDTO.getCustomerEmail());
        return orderEvent;
    }
}
