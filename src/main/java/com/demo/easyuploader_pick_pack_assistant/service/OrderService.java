package com.demo.easyuploader_pick_pack_assistant.service;

import com.demo.easyuploader_pick_pack_assistant.dto.GetOrderResponse;
import com.demo.easyuploader_pick_pack_assistant.model.Order;
import com.demo.easyuploader_pick_pack_assistant.repository.jpa.OrderRepository;
import com.demo.easyuploader_pick_pack_assistant.repository.query.OrderQueryDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.demo.easyuploader_pick_pack_assistant.controller.OrderDtoMapper.mapToOrderResponse;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderQueryDao orderQueryDao;
    private final OrderItemService orderItemService;

    public GetOrderResponse getOrder(String trackingNumber) {
        if (checkIfOrderIsImported(trackingNumber)){
            return mapToOrderResponse(orderRepository.findByTrackingNumber(trackingNumber));
        }
        else{
            Order order = processOrderFromEU(trackingNumber);
            return mapToOrderResponse(order);
        }
    }

    private boolean checkIfOrderIsImported(String trackingNumber){
        return orderRepository.existsByTrackingNumber(trackingNumber);
        //todo handle situation when order has more than one tracking number!
    }

    private Optional<Integer> getOrderIdFromEU(String trackingNumber){
        return orderQueryDao.findOrderId(trackingNumber);
    }

    private Order processOrderFromEU (String trackingNumber){
        Order order = new Order();
        order.setTrackingNumber(trackingNumber);
        Optional<Integer> orderId = getOrderIdFromEU(trackingNumber);
        if(orderId.isPresent()){
            order.setId(orderId.get().longValue());
            order.setUserId(1L); //todo change later to real user
            order.setBuyerLogin(orderQueryDao.findBuyerLoginByOrderId(orderId.get().longValue()));
            order.setOrderNotes(orderQueryDao.findOrderNotesByOrderId(orderId.get().longValue()));
            order.setGiftWrapping(orderQueryDao.findGiftWrappingByOrderId(orderId.get().longValue()));
            orderItemService.fillOrderItemsAndAttachToOrder(order);
            orderRepository.save(order);
            return order;
        }
        else{
            //todo improve handling of the situation when order is not found in EU db?
            return order;
        }

    }

}
