package com.blubank.store.model.service;

import com.blubank.store.controller.StoreController;
import com.blubank.store.exception.OrderNotFoundException;
import com.blubank.store.exception.UsernameNotFoundException;
import com.blubank.store.model.entity.Cart;
import com.blubank.store.model.entity.Order;
import com.blubank.store.model.entity.User;
import com.blubank.store.model.repository.CartRepository;
import com.blubank.store.model.repository.OrderRepository;
import com.blubank.store.model.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@Transactional
public class OrderService {

    private final static Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public OrderService(CartRepository cartRepository, OrderRepository orderRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    public void completeShopping() {
        User user = userRepository.findByUsername("admin");
        if(user == null) {
            throw new UsernameNotFoundException("User not found with username: admin");
        }
        Order order=orderRepository.getByUserAndStatus(user,"paying").get(0);
        if(order == null) {
            throw new OrderNotFoundException("user: " + user.getUsername() + " exception: order not found for user: admin");
        }
        order.setStatus("paid");
        logger.info("order paid");
        orderRepository.save(order);
        Cart cart = cartRepository.getByUser(user);
        cart.setProducts(new ArrayList<>());
        logger.info("products remove from cart");
        cartRepository.save(cart);
    }

}
