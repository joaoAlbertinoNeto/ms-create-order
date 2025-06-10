package br.com.order.ms_order.application.services;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import br.com.order.ms_order.application.ports.out.CreateOrderPortOut;
import br.com.order.ms_order.domain.dto.OrderCreatedDTO;
import br.com.order.ms_order.domain.dto.OrderDTO;
import br.com.order.ms_order.infrastructure.adapters.out.bd.repository.OrderRepository;

@Component
public class OrderServiceEventImpl implements OrderService {

    private final CreateOrderPortOut createOrderPortOutEvt;
    private final OrderRepository orderRepository;

    public OrderServiceEventImpl(@Qualifier("CreateOrderEventOutImpl")CreateOrderPortOut createOrderPortOutEvt , 
                                 OrderRepository orderRepository) {
        this.createOrderPortOutEvt = createOrderPortOutEvt;
        this.orderRepository = orderRepository;
    }

    @Scheduled(fixedDelay = 2000)
    @Override
    public OrderCreatedDTO createOrder(OrderDTO orderDTO) {
        try {
            var listOrders = orderRepository.findByCustomerByStatus("PENDING");
            if (listOrders.isEmpty()) {
                throw new RuntimeException("No pending orders found for the customer.");
            }

            for (var order : listOrders) {
                // Process each order as needed, e.g., log or update status
                System.out.println("Processing order: " + order.getId());
                createOrderPortOutEvt.createOrder(orderDTO);
            }
            return null;
        } catch (Exception e) {
            // Handle the exception appropriately, e.g., log it or rethrow it
            throw new RuntimeException("Error creating order: " + e.getMessage(), e);
        }
    }

}
