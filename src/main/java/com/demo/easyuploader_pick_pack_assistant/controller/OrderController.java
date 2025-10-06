package com.demo.easyuploader_pick_pack_assistant.controller;

import com.demo.easyuploader_pick_pack_assistant.dto.AuthenticatedUser;
import com.demo.easyuploader_pick_pack_assistant.dto.GetOrderResponse;
import com.demo.easyuploader_pick_pack_assistant.service.OrderItemService;
import com.demo.easyuploader_pick_pack_assistant.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final OrderItemService orderItemService;

    @GetMapping("/orders")
    public ResponseEntity<GetOrderResponse> getOrder(@RequestParam String identifier, @AuthenticationPrincipal AuthenticatedUser user) {
        GetOrderResponse getOrderResponse = orderService.getOrder(identifier, user.id());
        if (!getOrderResponse.items().isEmpty()) {
            return ResponseEntity.ok(getOrderResponse);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/orders/items/images/{model}")
    public ResponseEntity<byte[]> getOrderItemImage(@PathVariable String model) {
        return orderItemService.getOrderItemImage(model);
    }

    @PatchMapping("/orders/items/{orderItemId}/complete")
    public ResponseEntity<GetOrderResponse> markOrderItemAsCompleted(@PathVariable Long orderItemId) {
        GetOrderResponse getOrderResponse = orderService.markOrderItemAsCompleted(orderItemId);
        return ResponseEntity.ok(getOrderResponse);
    }
}
