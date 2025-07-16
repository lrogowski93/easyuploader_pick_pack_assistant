package com.demo.easyuploader_pick_pack_assistant.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "PICKPACKER_ORDER_ITEM")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderId;
    private String model;
    private String name;
    private String barcode;
    private int quantity;
    private String warehouseLocation;
    private Boolean giftWrapping;
    private String pictureUrl;
    private String notes;
    private Boolean isCompleted;
}
