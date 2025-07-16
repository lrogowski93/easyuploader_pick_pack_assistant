package com.demo.easyuploader_pick_pack_assistant.controller.response;

import com.demo.easyuploader_pick_pack_assistant.model.OrderItem;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
@Builder
public record GetOrderResponse(
        List<OrderItem> items,
        String trackingNumber,
        Boolean isCompleted,
        LocalDateTime pickPackStartTime,
        LocalDateTime completionTime,
        String pickPacker,
        String buyerLogin,
        String orderNotes) {
}
