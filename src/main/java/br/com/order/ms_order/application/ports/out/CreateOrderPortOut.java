package br.com.order.ms_order.application.ports.out;

import br.com.order.ms_order.domain.dto.OrderCreatedDTO;
import br.com.order.ms_order.domain.dto.OrderDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface CreateOrderPortOut {
    OrderCreatedDTO createOrder (OrderDTO orderDTO) throws JsonProcessingException;
}
