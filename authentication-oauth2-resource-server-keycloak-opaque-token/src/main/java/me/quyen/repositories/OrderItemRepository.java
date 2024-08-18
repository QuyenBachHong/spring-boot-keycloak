package me.quyen.repositories;

import java.util.List;

import me.quyen.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;



public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{

    List<OrderItem> findByOrderId(Long id);

}