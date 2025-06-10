package br.com.order.ms_order.application.services;

import br.com.order.ms_order.application.ports.out.CreateOrderPortOut;
import br.com.order.ms_order.domain.dto.OrderCreatedDTO;
import br.com.order.ms_order.domain.dto.OrderDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService{
    private final CreateOrderPortOut createOrderPortOutBd;

    public OrderServiceImpl(@Qualifier("OrderBdImpl") CreateOrderPortOut createOrderPortOutBd) {
        this.createOrderPortOutBd = createOrderPortOutBd;
    }

    @Override
    public OrderCreatedDTO createOrder(OrderDTO orderDTO) {
        try {
            return createOrderPortOutBd.createOrder(orderDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
