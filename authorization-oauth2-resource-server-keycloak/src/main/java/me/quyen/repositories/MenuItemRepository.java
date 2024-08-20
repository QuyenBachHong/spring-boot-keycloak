package me.quyen.repositories;

import me.quyen.entities.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;



public interface MenuItemRepository extends JpaRepository<MenuItem, Long>{

    List<MenuItem> findAllByMenuId(Long id);

}