package br.com.order.ms_order.infrastructure.adapters.in.rest;

import br.com.order.ms_order.application.services.OrderService;
import br.com.order.ms_order.domain.dto.OrderCreatedDTO;
import br.com.order.ms_order.domain.dto.OrderDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController("/orders")
public class OrderControllerImpl implements OrderController {
    OrderService service;

    @Override
    public ResponseEntity<OrderCreatedDTO> createOrder(OrderDTO dto) {
        return ResponseEntity.ok(service.createOrder(dto));
    }
}
