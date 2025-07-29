package com.demo.easyuploader_pick_pack_assistant.repository.jpa;

import com.demo.easyuploader_pick_pack_assistant.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
