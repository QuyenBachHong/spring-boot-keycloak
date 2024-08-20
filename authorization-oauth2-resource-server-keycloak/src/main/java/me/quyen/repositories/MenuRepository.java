package me.quyen.repositories;

import me.quyen.entities.Menu;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MenuRepository  extends JpaRepository<Menu, Long>{

    Menu findByRestaurantId(Long restaurantId);
}