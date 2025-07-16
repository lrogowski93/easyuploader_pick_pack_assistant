package com.demo.easyuploader_pick_pack_assistant.controller;

import com.demo.easyuploader_pick_pack_assistant.controller.response.GetOrderResponse;
import com.demo.easyuploader_pick_pack_assistant.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/orders/{trackingNumber}")
    public ResponseEntity<GetOrderResponse> getOrder(@PathVariable String trackingNumber) {
        //todo check if order exist in pickpacker table, if not - search EU table and add new order in pickpacker table ELSE throw 404
        GetOrderResponse getOrderResponse = orderService.getOrder(trackingNumber);
        if (getOrderResponse != null) {
            return ResponseEntity.ok(getOrderResponse);
        }
        return ResponseEntity.notFound().build();
    }
}
