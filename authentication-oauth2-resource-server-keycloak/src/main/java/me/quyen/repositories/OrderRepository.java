package me.quyen.repositories;

import java.util.List;

import me.quyen.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<Order, Long>{

    List<Order> findByRestaurantId(Long restaurantId);

}