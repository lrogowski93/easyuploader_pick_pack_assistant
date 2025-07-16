package com.demo.easyuploader_pick_pack_assistant.service;

import com.demo.easyuploader_pick_pack_assistant.controller.response.GetOrderResponse;
import com.demo.easyuploader_pick_pack_assistant.model.Order;
import com.demo.easyuploader_pick_pack_assistant.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import static com.demo.easyuploader_pick_pack_assistant.controller.OrderDtoMapper.*;
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    public GetOrderResponse getOrder(String trackingNumber) {
        Order order = orderRepository.findByTrackingNumber(trackingNumber);
        if (order != null)
        return mapToOrderResponse(order);
        else return null;
    }
}
