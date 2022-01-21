package com.blubank.store.model.repository;

import com.blubank.store.model.entity.Cart;
import com.blubank.store.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart getByUser(User user);
}
