package com.demo.easyuploader_pick_pack_assistant.controller;

import com.demo.easyuploader_pick_pack_assistant.dto.GetOrderResponse;
import com.demo.easyuploader_pick_pack_assistant.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/orders/{trackingNumber}")
    public ResponseEntity<GetOrderResponse> getOrder(@PathVariable String trackingNumber) {

        GetOrderResponse getOrderResponse = orderService.getOrder(trackingNumber);
        if (!getOrderResponse.items().isEmpty()) {
            return ResponseEntity.ok(getOrderResponse);
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/orders/items/{orderItemId}/complete")
    public ResponseEntity<GetOrderResponse> markOrderItemAsCompleted(@PathVariable Long orderItemId) {

        GetOrderResponse getOrderResponse = orderService.markOrderItemAsCompleted(orderItemId);
        return ResponseEntity.ok(getOrderResponse);
    }
}
