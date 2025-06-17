package br.com.order.ms_order.application.services;

import br.com.order.ms_order.domain.dto.OrderCreatedDTO;
import br.com.order.ms_order.domain.dto.OrderDTO;

public interface OrderService {
    OrderCreatedDTO createOrder (OrderDTO orderDTO);
    OrderCreatedDTO getByOrderId(String id);
}
