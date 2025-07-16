package com.demo.easyuploader_pick_pack_assistant.repository;

import com.demo.easyuploader_pick_pack_assistant.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByTrackingNumber(String trackingNumber);
}
