package com.demo.easyuploader_pick_pack_assistant.controller;

import com.demo.easyuploader_pick_pack_assistant.controller.response.GetOrderResponse;
import com.demo.easyuploader_pick_pack_assistant.model.Order;

public class OrderDtoMapper {
    public static GetOrderResponse mapToOrderResponse(Order order) {

        return GetOrderResponse.builder()
                .items(null) //todo change to real items after implementation
                .trackingNumber(order.getTrackingNumber())
                .isCompleted(order.getIsCompleted())
                .pickPackStartTime(order.getPickPackStartTime())
                .completionTime(order.getCompletionTime())
                .pickPacker("username") //todo retrieve name from db by id
                .buyerLogin(order.getBuyerLogin())
                .orderNotes(order.getOrderNotes())
                .build();
    }
}
