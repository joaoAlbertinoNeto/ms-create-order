package br.com.order.ms_order.application.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.order.ms_order.application.ports.out.CreateOrderPortOut;
import br.com.order.ms_order.domain.dto.OrderDTO;
import br.com.order.ms_order.infrastructure.adapters.out.bd.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("OrderServiceEventImpl")
public class OrderServiceEventImpl {

    private CreateOrderPortOut createOrderPortOutEvt;
    private OrderRepository orderRepository;
    private ModelMapper modelMapper;
    

    @Autowired
    public OrderServiceEventImpl(@Qualifier("CreateOrderEventOutImpl")CreateOrderPortOut createOrderPortOutEvt , 
                                 OrderRepository orderRepository,
                                 ModelMapper modelMapper) {
        this.createOrderPortOutEvt = createOrderPortOutEvt;
        this.orderRepository = orderRepository; 
        this.modelMapper = modelMapper;
    }

    @Scheduled(fixedDelay = 5000)
    public void createOrder() {
        try {
            var listOrders = orderRepository.findByStatus("PENDING");
            if (listOrders.isEmpty()) {
                log.info("[EVENT] - No pending orders to process.");
                return;
            }

            for (var orderEntity : listOrders) {
                log.info("[EVENT] - Processing order: " + orderEntity.getId());
                createOrderPortOutEvt.createOrder(modelMapper.map(orderEntity , OrderDTO.class));
                orderEntity.setStatus("SENT_TO_PROCESSING");
                orderRepository.save(orderEntity);
            }
            return;
        } catch (Exception e) {
            throw new RuntimeException("[EVENT] - Error creating order: " + e.getMessage(), e);
        }
    }

}