package com.demo.easyuploader_pick_pack_assistant.dto;

import com.demo.easyuploader_pick_pack_assistant.model.OrderItem;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
@Builder
public record GetOrderResponse(
        List<OrderItem> items,
        List<String> trackingNumbers,
        boolean isCompleted,
        LocalDateTime pickPackStartTime,
        LocalDateTime completionTime,
        String pickPacker,
        String buyerLogin,
        String orderNotes,
        String giftWrapping
        ) {
}
