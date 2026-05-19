package se.iths.erikthorell.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.iths.erikthorell.orderservice.entity.CustomerOrder;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {
}