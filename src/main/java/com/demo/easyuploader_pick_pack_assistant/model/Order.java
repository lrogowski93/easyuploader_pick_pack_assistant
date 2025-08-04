package com.demo.easyuploader_pick_pack_assistant.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "PICKPACKER_ORDER")
public class Order {
    @Id
    private Long id;
    private Long userId;
    //private String trackingNumber;
    private boolean isCompleted;
    private LocalDateTime pickPackStartTime;
    private LocalDateTime completionTime;
    private String buyerLogin;
    private String orderNotes;
    private String giftWrapping;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderItem> orderItems = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "PICKPACKER_ORDER_TRACKINGS", joinColumns = @JoinColumn(name = "order_id"))
    @Column(name = "tracking_number")
    private List<String> trackingNumbers = new ArrayList<>();

    public void addOrderItem(OrderItem item) {
        item.setOrder(this);
        this.orderItems.add(item);
    }

    public void addTrackingNumber(String trackingNumber) {
        if (!this.trackingNumbers.contains(trackingNumber)) {
            this.trackingNumbers.add(trackingNumber);
        }
    }
}
