package com.demo.easyuploader_pick_pack_assistant.service;

import com.demo.easyuploader_pick_pack_assistant.dto.OrderItemDto;
import com.demo.easyuploader_pick_pack_assistant.model.Order;
import com.demo.easyuploader_pick_pack_assistant.model.OrderItem;
import com.demo.easyuploader_pick_pack_assistant.repository.query.OrderQueryDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class OrderItemService {
    private final OrderQueryDao orderQueryDao;


    private String getOrderItemWarehouseLocation(String model){
        return orderQueryDao.findOrderItemWarehouseLocation(model);
    }

    private int getOrderItemQuantity(String trackingNumber, String model){
        return orderQueryDao.findOrderItemQuantity(trackingNumber,model);
    }

    private OrderItemDto getOrderItemFromEU(long orderId){
        //method returns single object, but because of the DB specific logic, there could be many items grouped in one row,
        //so it has to be checked and processed later
        return orderQueryDao.findOrderItemsFromEU(orderId);
    }

    public void fillOrderItemsAndAttachToOrder(Order order) {
        OrderItemDto orderItemDto = getOrderItemFromEU(order.getId());
        if (checkIfThereIsMoreThanOneItem(orderItemDto)) {
            Arrays.stream(orderItemDto.model().split(", "))
                    .distinct()
                    .map(model -> OrderItem.builder()
                            .model(model)
                            .name(getOrderItemName(order.getTrackingNumbers().get(0), model))
                            .barcode(getOrderItemBarcode(order.getTrackingNumbers().get(0), model))
                            .quantity(getOrderItemQuantity(order.getTrackingNumbers().get(0), model))
                            .warehouseLocation(getOrderItemWarehouseLocation(model))
                            .build())
                    .forEach(order::addOrderItem);
        } else {
            order.addOrderItem(
                    OrderItem.builder()
                            .model(orderItemDto.model())
                            .name(orderItemDto.name())
                            .barcode(orderItemDto.barcode())
                            .quantity(orderItemDto.quantity())
                            .warehouseLocation(getOrderItemWarehouseLocation(orderItemDto.model()))
                            .build()
            );
        }

    }

    private String getOrderItemBarcode(String trackingNumber, String model) {
        return orderQueryDao.findOrderItemBarcode(trackingNumber,model);
    }

    private String getOrderItemName(String trackingNumber, String model) {
        return orderQueryDao.findOrderItemName(trackingNumber,model);
    }

    private boolean checkIfThereIsMoreThanOneItem(OrderItemDto orderItemDto){
        return orderItemDto.model().contains(",");
    }
}
