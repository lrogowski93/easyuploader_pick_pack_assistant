package com.demo.easyuploader_pick_pack_assistant.service;

import com.demo.easyuploader_pick_pack_assistant.dto.GetOrderResponse;
import com.demo.easyuploader_pick_pack_assistant.model.Order;
import com.demo.easyuploader_pick_pack_assistant.model.OrderIdentifier;
import com.demo.easyuploader_pick_pack_assistant.model.OrderItem;
import com.demo.easyuploader_pick_pack_assistant.repository.jpa.OrderItemRepository;
import com.demo.easyuploader_pick_pack_assistant.repository.jpa.OrderRepository;
import com.demo.easyuploader_pick_pack_assistant.repository.query.OrderQueryDao;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;


import static com.demo.easyuploader_pick_pack_assistant.controller.OrderDtoMapper.mapToOrderResponse;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderQueryDao orderQueryDao;
    private final OrderItemService orderItemService;


    public GetOrderResponse getOrder(String input) {
        OrderIdentifier identifier = OrderIdentifier.of(input);

        if (checkIfOrderIsImported(identifier)){
            if(identifier.isEuid()) {
                return mapToOrderResponse(orderRepository.findById(identifier.euIdValue()).orElseThrow());
            }
            else{
                return mapToOrderResponse(orderRepository.findByTrackingNumber(identifier.normalized()));
            }
        }
        else{
            Order order = processOrderFromEU(identifier);
            return mapToOrderResponse(order);
        }
    }


    private boolean checkIfOrderIsImported(OrderIdentifier identifier){
        if(identifier.isEuid()){
            return orderRepository.existsById(identifier.euIdValue());
        }
        else {
            return orderRepository.existsByTrackingNumber(identifier.normalized());
        }
    }


    private Optional<Integer> getOrderIdFromEU(String trackingNumber){
        return orderQueryDao.findOrderId(trackingNumber);
    }

    private Order processOrderFromEU (OrderIdentifier identifier){
        Long orderId;
        if(identifier.isEuid()){
            orderId = identifier.euIdValue();
        }
        else{
            Optional<Integer> orderIdOpt = getOrderIdFromEU(identifier.normalized());
            if (orderIdOpt.isEmpty()) {
                return new Order();
            }
            orderId = orderIdOpt.get().longValue();
        }

        Order order = orderRepository.findById(orderId).orElseGet(() -> {
            Order newOrder = new Order();
            newOrder.setId(orderId);
            newOrder.setUserId(1L); //todo change later to real user
            newOrder.setBuyerLogin(orderQueryDao.findBuyerLoginByOrderId(orderId));
            newOrder.setOrderNotes(orderQueryDao.findOrderNotesByOrderId(orderId));
            newOrder.setGiftWrapping(orderQueryDao.findGiftWrappingByOrderId(orderId));
            return newOrder;
        });
        if(identifier.isEuid()) {
            order.addTrackingNumber(orderQueryDao.findFirstTrackingNumberByOrderId(orderId));
        }
        else {
            order.addTrackingNumber(identifier.normalized());
        }
        if (order.getOrderItems().isEmpty()) {
            orderItemService.fillOrderItemsAndAttachToOrder(order);
        }

        return orderRepository.save(order);

    }

    public GetOrderResponse markOrderItemAsCompleted(Long orderItemId) {
        OrderItem item = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new EntityNotFoundException("OrderItem not found"));

        item.setCompleted(true);

        Order order = item.getOrder();
        if (order.getPickPackStartTime() == null) {
            order.setPickPackStartTime(LocalDateTime.now());
        }
        if(order.getCompletionTime() == null && checkIfAllOrderItemsAreCompleted(order)){
            order.setCompleted(true);
            order.setCompletionTime(LocalDateTime.now());
        }
        orderRepository.save(order);
        return mapToOrderResponse(order);
    }

    private boolean checkIfAllOrderItemsAreCompleted(Order order) {
        return order.getOrderItems().stream().allMatch(OrderItem::isCompleted);
    }
}
