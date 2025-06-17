package br.com.order.ms_order.application.services;

import br.com.order.ms_order.application.ports.out.CreateOrderPortOut;
import br.com.order.ms_order.domain.dto.OrderCreatedDTO;
import br.com.order.ms_order.domain.dto.OrderDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Slf4j
@Component("OrderServiceImpl")
public class OrderServiceImpl implements OrderService{
    private final CreateOrderPortOut createOrderPortOutBd;

    @Autowired
    public OrderServiceImpl(@Qualifier("OrderBdImpl") CreateOrderPortOut createOrderPortOutBd) {
        this.createOrderPortOutBd = createOrderPortOutBd;
    }

    @Override
    public OrderCreatedDTO createOrder(OrderDTO orderDTO) {
        try {
            log.info("[SERVICE_BD] - Creating order with code: {}", orderDTO.getCode());
            return createOrderPortOutBd.createOrder(orderDTO);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public OrderCreatedDTO getByOrderId(String id) {
        try {
            log.info("[SERVICE_BD] - Getting order with id: {}", id);
            return createOrderPortOutBd.getByOrderId(id);
        } catch (Exception e) {
            log.error("[SERVICE_BD] - Error getting order: {}", e.getMessage());
            throw new RuntimeException("Error getting order: " + e.getMessage(), e);
        }
    }
}
