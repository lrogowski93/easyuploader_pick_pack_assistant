package com.demo.easyuploader_pick_pack_assistant.repository.jpa;

import com.demo.easyuploader_pick_pack_assistant.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    //Order findByTrackingNumber(String trackingNumber);

    //boolean existsByTrackingNumber(String trackingNumber);

    @Query("""
    SELECT o FROM Order o
    JOIN o.trackingNumbers tn
    WHERE tn = :trackingNumber
""")
    Order findByTrackingNumber(@Param("trackingNumber") String trackingNumber);
    @Query("""
    SELECT count(o) > 0 FROM Order o
    JOIN o.trackingNumbers tn
    WHERE tn = :trackingNumber
""")
    boolean existsByTrackingNumber(@Param("trackingNumber") String trackingNumber);
}
