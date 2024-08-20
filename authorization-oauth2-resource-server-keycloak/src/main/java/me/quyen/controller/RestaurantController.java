package me.quyen.controller;

import lombok.RequiredArgsConstructor;
import me.quyen.entities.Menu;
import me.quyen.entities.MenuItem;
import me.quyen.entities.Restaurant;
import me.quyen.repositories.MenuItemRepository;
import me.quyen.repositories.MenuRepository;
import me.quyen.repositories.RestaurantRepository;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/restaurant")
@RequiredArgsConstructor
@io.swagger.v3.oas.annotations.security.SecurityRequirement(
        name = "springboot_keycloak"
)
public class RestaurantController {
    final RestaurantRepository restaurantRepository;
    final MenuRepository menuRepository;
    final MenuItemRepository menuItemRepository;

    @GetMapping
    @RequestMapping("/public/list")
    //Public API
    public List<Restaurant> getRestaurants() {
        return restaurantRepository.findAll();
    }

    @GetMapping
    @RequestMapping("/public/menu/{restaurantId}")
    //Public API
    public Menu getMenu(@PathVariable Long restaurantId) {
        Menu menu = menuRepository.findByRestaurantId(restaurantId);
        menu.setMenuItems(menuItemRepository.findAllByMenuId(menu.id));
        return menu;
    }

    @PostMapping
    // admin can access (admin_user)
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
    public Restaurant createRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    @PutMapping
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('MANAGER')")
    // manager can access (suresh::manager_user)
    public Restaurant updateRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    @PostMapping
    @RequestMapping("/menu")
    // manager can access (suresh::manager)
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('MANAGER')")
    public Menu createMenu(Menu menu) {
        menuRepository.save(menu);
        menu.getMenuItems().forEach(menuItem -> {
            menuItem.setMenuId(menu.id);
            menuItemRepository.save(menuItem);
        });
        return menu;
    }

    @PutMapping
    @RequestMapping("/menu/item/{itemId}/{price}")
    // owner can access (amar::ower)
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('OWNER')")
    public MenuItem updateMenuItemPrice(@PathVariable("itemId") Long itemId
            , @PathVariable("price") BigDecimal price) {
        Optional<MenuItem> menuItem = menuItemRepository.findById(itemId);
        menuItem.get().setPrice(price);
        menuItemRepository.save(menuItem.get());
        return menuItem.get();
    }

}