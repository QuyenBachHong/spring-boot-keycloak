package me.quyen.repositories;

import me.quyen.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;



public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{

    List<OrderItem> findByOrderId(Long id);

}