package br.com.order.ms_order.infrastructure.adapters.out.bd.repository;

import br.com.order.ms_order.infrastructure.adapters.out.bd.dto.OrderEntityDTO;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends MongoRepository<OrderEntityDTO,String> {
    public List<OrderEntityDTO> findByStatus(String status);
    
    // Find orders by customer ID
    OrderEntityDTO findOneByCode(String code);
    
}
