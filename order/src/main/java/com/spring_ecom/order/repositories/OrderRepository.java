package com.spring_ecom.order.repositories;

import com.spring_ecom.order.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository <Order, Long>{
}
