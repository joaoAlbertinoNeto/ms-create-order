package br.com.order.ms_order.infrastructure.adapters.in.rest;

import br.com.order.ms_order.application.services.OrderService;
import br.com.order.ms_order.domain.dto.OrderCreatedDTO;
import br.com.order.ms_order.domain.dto.OrderDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/order")
public class OrderControllerImpl implements OrderController {
    
    @Qualifier("OrderServiceImpl")
    @Autowired
    OrderService service;

    @Override
    public ResponseEntity<OrderCreatedDTO> createOrder(OrderDTO dto) {
        OrderCreatedDTO createdOrder = service.createOrder(dto);
        String orderLink = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(createdOrder.getId())
            .toUriString();
        createdOrder.setOrderLink(orderLink);
        return ResponseEntity.ok(createdOrder);
    }

    @Override
    public ResponseEntity<OrderCreatedDTO> getByOrderId(String id) {
        return ResponseEntity.ok(service.getByOrderId(id));
    }
}
