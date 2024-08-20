package me.quyen.controller;

import lombok.RequiredArgsConstructor;
import me.quyen.entities.Order;
import me.quyen.entities.OrderItem;
import me.quyen.repositories.OrderItemRepository;
import me.quyen.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
@io.swagger.v3.oas.annotations.security.SecurityRequirement(
        name = "springboot_keycloak"
)
public class OrderController {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @GetMapping
    @RequestMapping("/{restaurantId}/list")
    // manager can access (suresh::manager_user)
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('MANAGER')")
    public List<Order> getOrders(@PathVariable Long restaurantId) {
        return orderRepository.findByRestaurantId(restaurantId);
    }

    @GetMapping
    @RequestMapping("/{orderId}")
    // manager can access (suresh::manager_user)
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('MANAGER')")
    public Order getOrderDetails(@PathVariable Long orderId) {
        Order order = orderRepository.findById(orderId).get();
        order.setOrderItems(orderItemRepository.findByOrderId(order.getId()));
        return order;
    }

    @PostMapping
    // authenticated users can access
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('AUTHENTICATED')")
    public Order createOrder(Order order) {
        orderRepository.save(order);
        List<OrderItem> orderItems = order.getOrderItems();
        orderItems.forEach(orderItem -> {
            orderItem.setOrderId(order.id);
            orderItemRepository.save(orderItem);
        });
        return order;
    }

}