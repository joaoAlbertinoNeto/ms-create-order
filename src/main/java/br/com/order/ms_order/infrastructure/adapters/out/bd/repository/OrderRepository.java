package br.com.order.ms_order.infrastructure.adapters.out.bd.repository;

import br.com.order.ms_order.infrastructure.adapters.out.bd.dto.OrderEntityDTO;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends MongoRepository<OrderEntityDTO,Long> {
    public List<OrderEntityDTO> findByStatus(String status);
}
