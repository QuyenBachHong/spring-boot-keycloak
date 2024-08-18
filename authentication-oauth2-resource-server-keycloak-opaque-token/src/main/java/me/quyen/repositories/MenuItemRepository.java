package me.quyen.repositories;

import java.util.List;

import me.quyen.entities.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;



public interface MenuItemRepository extends JpaRepository<MenuItem, Long>{

    List<MenuItem> findAllByMenuId(Long id);

}