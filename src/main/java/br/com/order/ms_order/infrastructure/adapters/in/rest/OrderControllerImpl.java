package br.com.order.ms_order.infrastructure.adapters.in.rest;

import br.com.order.ms_order.application.services.OrderService;
import br.com.order.ms_order.domain.dto.OrderCreatedDTO;
import br.com.order.ms_order.domain.dto.OrderDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/order")
@Tag(name = "Order Management", description = "APIs para gerenciamento de pedidos")
@SecurityRequirement(name = "bearerAuth")
public class OrderControllerImpl implements OrderController {
    
    @Qualifier("OrderServiceImpl")
    @Autowired
    OrderService service;

    @Override
    @Operation(summary = "Criar novo pedido", description = "Cria um novo pedido no sistema")
    @PreAuthorize("hasAuthority('writter') or hasRole('USER') or hasRole('ADMIN')")
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
    @Operation(summary = "Buscar pedido por ID", description = "Recupera um pedido específico pelo seu ID")
    @PreAuthorize("hasAuthority('writter') or hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<OrderCreatedDTO> getByOrderId(String id) {
        return ResponseEntity.ok(service.getByOrderId(id));
    }
}
