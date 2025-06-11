package br.com.order.ms_order.infrastructure.adapters.out.bd;

import br.com.order.ms_order.application.ports.out.CreateOrderPortOut;
import br.com.order.ms_order.domain.dto.OrderCreatedDTO;
import br.com.order.ms_order.domain.dto.OrderDTO;
import br.com.order.ms_order.infrastructure.adapters.out.bd.mapper.BdMapper;
import br.com.order.ms_order.infrastructure.adapters.out.bd.repository.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component("OrderBdImpl")
public class OrderBdImpl implements CreateOrderPortOut {

    private final OrderRepository orderRepository;

    private final BdMapper mapper;
    @Override
    public OrderCreatedDTO createOrder(OrderDTO orderDTO) throws JsonProcessingException {
        try{
            var saved = orderRepository.save(mapper.mapToEntity(orderDTO));
            log.info("Saved order to database: {}", orderDTO.getCode());
            return mapper.mapFromEntity(saved);
        }catch (Exception e){
            log.error("Error to save : {}",e.getLocalizedMessage());
            throw e;
        }
    }
}
