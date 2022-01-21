package com.blubank.store.controller;

import com.blubank.store.exception.ProductNotFoundException;
import com.blubank.store.exception.UsernameNotFoundException;
import com.blubank.store.model.entity.Cart;
import com.blubank.store.model.entity.Order;
import com.blubank.store.model.entity.Product;
import com.blubank.store.model.entity.User;
import com.blubank.store.model.repository.CartRepository;
import com.blubank.store.model.repository.OrderRepository;
import com.blubank.store.model.repository.ProductRepository;
import com.blubank.store.model.repository.UserRepository;
import com.blubank.store.model.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
@RequestMapping("/store")
public class StoreController {

    private final static Logger logger = LoggerFactory.getLogger(StoreController.class);

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderService orderService;

    public StoreController(ProductRepository productRepository, CartRepository cartRepository, OrderRepository orderRepository, UserRepository userRepository, OrderService orderService) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.orderService = orderService;
    }

    @PostConstruct
    public void init() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("admin");
        Cart cart = new Cart();
        cart.setUser(user);
        cartRepository.save(cart);
        logger.info("user : " + user.getUsername() + " saved.");
        Product product = new Product();
        product.setTitle("book");
        product.setPrice(50000);
        productRepository.save(product);
    }

    @GetMapping("/products")
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @PostMapping("/cart/{productId}")
    public void addProductToCart(@PathVariable Long productId) {
        User user = getUser();
        Cart cart = cartRepository.getByUser(user);
        Product product = productRepository.getById(productId);
        if(product.getId() == null) {
            throw new ProductNotFoundException("user: " + user.getUsername() + " exception: product not found for user admin");
        }
        cart.getProducts().add(product);
        logger.info("add product: " + product.getTitle() + " to user: " + user.getUsername() + " cart");
        cartRepository.save(cart);
    }

    private User getUser() {
        User user = userRepository.findByUsername("admin");
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: admin");
        }
        return user;
    }

    @GetMapping("/cart")
    public Cart getCart() {
        User user = getUser();
        return cartRepository.getByUser(user);
    }

    @PostMapping("/order")
    public void addOrder() {
        User user = getUser();
        Cart cart = cartRepository.getByUser(user);
        Order order = new Order();
        List<Product> products = cart.getProducts();
        if (products.isEmpty()) {
            throw new ProductNotFoundException("user: " + user.getUsername() + " exception: product not found for user admin");
        }
        order.getProducts().addAll(products);
        order.setUser(user);
        order.setStatus("paying");
        logger.info("paying order from user: " + user.getUsername() + " cart");
        orderRepository.save(order);
    }

    @GetMapping("/order/paying")
    public Order getOrder() {
        User user = getUser();
        List<Order> orders= orderRepository.getByUserAndStatus(user,"paying");
        if(orders.isEmpty()){
            return new Order();
        }
        return orders.get(0);
    }

    @PutMapping("/order")
    public void completeShopping() {
        orderService.completeShopping();
    }

    @GetMapping("/orders/paid")
    public List<Order> getPaidOrders(){
        User user=userRepository.findByUsername("admin");
        return orderRepository.getByUserAndStatus(user,"paid");
    }

    @DeleteMapping("/cart/{productId}")
    public void deleteProductFromCart(@PathVariable Long productId) {
        User user = getUser();
        Cart cart = cartRepository.getByUser(user);
        Product product = productRepository.getById(productId);
        if(product.getId() == null) {
            throw new ProductNotFoundException("user: " + user.getUsername() + " exception: product not found for user admin");
        }
        cart.getProducts().remove(product);
        logger.info("remove product: " + product.getTitle() + " from user: " + user.getUsername() + " cart");
        cartRepository.save(cart);
    }

}
