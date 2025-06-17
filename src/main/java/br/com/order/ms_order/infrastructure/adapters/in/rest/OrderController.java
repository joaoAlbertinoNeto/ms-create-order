package br.com.order.ms_order.infrastructure.adapters.in.rest;

import br.com.order.ms_order.domain.dto.OrderCreatedDTO;
import br.com.order.ms_order.domain.dto.OrderDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface OrderController  {

    @ApiOperation("Creates a new Order")
    @PostMapping("/create")
    public ResponseEntity<OrderCreatedDTO> createOrder(@RequestBody OrderDTO dto);

    @ApiOperation("Get order by ID")
    @GetMapping("/{id}")
    public ResponseEntity<OrderCreatedDTO> getByOrderId(@PathVariable String id);

}
