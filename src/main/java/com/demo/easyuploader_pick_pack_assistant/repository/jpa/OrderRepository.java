package com.demo.easyuploader_pick_pack_assistant.repository.jpa;

import com.demo.easyuploader_pick_pack_assistant.model.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
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

    List<Order> findAllByPickPackerIdAndIsCompletedTrueAndCompletionTimeBetween(
            Long pickPackerId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    List<Order> findAllByPickPackerIdAndIsCompletedTrueAndCompletionTimeBetween(
            Long pickPackerId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable page
    );

    List<Order> findAllByPickPackerIdAndIsCompletedTrueAndCompletionTimeBetweenAndLargeSizeOrderTrue(
            Long pickPackerId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable page
    );
}
