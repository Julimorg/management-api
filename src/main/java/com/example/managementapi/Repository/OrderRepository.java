package com.example.managementapi.Repository;


import com.example.managementapi.Entity.Cart;
import com.example.managementapi.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    Optional<Order> findByUserId(String userId);

}
