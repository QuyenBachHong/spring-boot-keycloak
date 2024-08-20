package me.quyen.repositories;

import me.quyen.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface OrderRepository extends JpaRepository<Order, Long>{

    List<Order> findByRestaurantId(Long restaurantId);

}