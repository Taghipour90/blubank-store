package com.blubank.store.model.repository;

import com.blubank.store.model.entity.Order;
import com.blubank.store.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> getByUserAndStatus(User user, String status);
}
