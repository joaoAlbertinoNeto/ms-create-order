package br.com.order.ms_order.infrastructure.adapters.out.bd.mapper;

import br.com.order.ms_order.domain.dto.OrderCreatedDTO;
import br.com.order.ms_order.domain.dto.OrderDTO;
import br.com.order.ms_order.infrastructure.adapters.out.bd.dto.OrderEntityDTO;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BdMapper {

    public OrderEntityDTO mapToEntity(OrderDTO orderDTO){
        var orderEntityDTO = new OrderEntityDTO();
        orderEntityDTO.setCode(orderDTO.getCode());
        orderEntityDTO.setStatus(orderDTO.getStatus());
        orderEntityDTO.setCreatedAt(orderDTO.getCreatedAt());
        orderEntityDTO.setCustomerTelephoneNumber(orderDTO.getCustomerTelephoneNumber());
        orderEntityDTO.setCustomerEmail(orderDTO.getCustomerEmail());
        orderEntityDTO.setUuid(String.valueOf(UUID.randomUUID()));
        return orderEntityDTO;
    }

    public OrderCreatedDTO mapFromEntity(OrderEntityDTO entityDTO){
        var orderCreatedDTO = new OrderCreatedDTO();
        orderCreatedDTO.setCode(entityDTO.getCode());
        orderCreatedDTO.setStatus(entityDTO.getStatus());
        orderCreatedDTO.setCreatedAt(entityDTO.getCreatedAt());
        orderCreatedDTO.setCustomerTelephoneNumber(entityDTO.getCustomerTelephoneNumber());
        orderCreatedDTO.setCustomerEmail(entityDTO.getCustomerEmail());
        orderCreatedDTO.setId(String.valueOf(entityDTO.getId()));
        return orderCreatedDTO;
    }

}