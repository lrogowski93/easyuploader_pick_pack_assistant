package com.demo.easyuploader_pick_pack_assistant.dto;

import lombok.Builder;

@Builder
public record OrderItemDto(
        Long orderId,
        String model,
        String name,
        String barcode,
        int quantity
) {
}
