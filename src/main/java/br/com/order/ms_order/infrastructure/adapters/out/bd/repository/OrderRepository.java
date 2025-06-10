package br.com.order.ms_order.infrastructure.adapters.out.bd.repository;

import br.com.order.ms_order.infrastructure.adapters.out.bd.dto.OrderEntityDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntityDTO,Long> {
}
