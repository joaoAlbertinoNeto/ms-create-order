package br.com.order.ms_order.application.services;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.com.order.ms_order.application.ports.out.CreateOrderPortOut;
import br.com.order.ms_order.domain.dto.OrderCreatedDTO;
import br.com.order.ms_order.domain.dto.OrderDTO;

@Service
public class OrderServiceEventImpl implements OrderService {

    private final CreateOrderPortOut createOrderPortOutEvt;

    public OrderServiceEventImpl(@Qualifier("CreateOrderEventOutImpl")CreateOrderPortOut createOrderPortOutEvt) {
        this.createOrderPortOutEvt = createOrderPortOutEvt;
    }

    @Scheduled(fixedDelay = 2000)
    @Override
    public OrderCreatedDTO createOrder(OrderDTO orderDTO) {
        try {
            return createOrderPortOutEvt.createOrder(orderDTO);
        } catch (Exception e) {
            // Handle the exception appropriately, e.g., log it or rethrow it
            throw new RuntimeException("Error creating order: " + e.getMessage(), e);
        }
    }

}
