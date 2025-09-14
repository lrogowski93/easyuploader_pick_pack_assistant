package com.demo.easyuploader_pick_pack_assistant.service;

import com.demo.easyuploader_pick_pack_assistant.dto.OrderItemDto;
import com.demo.easyuploader_pick_pack_assistant.model.Order;
import com.demo.easyuploader_pick_pack_assistant.model.OrderItem;
import com.demo.easyuploader_pick_pack_assistant.repository.query.OrderQueryDao;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
        if (checkIfThereIsMoreThanOneItem(orderItemDto.model())) {
            Arrays.stream(orderItemDto.model().split(", "))
                    .distinct()
                    .map(model -> OrderItem.builder()
                            .model(model)
                            .name(getOrderItemName(order.getTrackingNumbers().get(0), model))
                            .barcode(getOrderItemBarcode(model))
                            .quantity(getOrderItemQuantity(order.getTrackingNumbers().get(0), model))
                            .warehouseLocation(getOrderItemWarehouseLocation(model))
                            .pictureUrl("/orders/items/images/"+model)
                            .build())
                    .forEach(order::addOrderItem);
        } else {
            order.addOrderItem(
                    OrderItem.builder()
                            .model(orderItemDto.model())
                            .name(orderItemDto.name())
                            .barcode(getOrderItemBarcode(orderItemDto.model()))
                            .quantity(orderItemDto.quantity())
                            .warehouseLocation(getOrderItemWarehouseLocation(orderItemDto.model()))
                            .pictureUrl("/orders/items/images/"+orderItemDto.model())
                            .build()
            );
        }

    }

    private String getOrderItemBarcode(String model) {
        return orderQueryDao.findOrderItemBarcode(model);
    }

    private String getOrderItemName(String trackingNumber, String model) {
        return orderQueryDao.findOrderItemName(trackingNumber,model);
    }

    private boolean checkIfThereIsMoreThanOneItem(String orderItemDtoModel){
        return orderItemDtoModel.contains(",");
    }

    public ResponseEntity<byte[]> getOrderItemImage(String model) {
        //todo resolve image of actual item from the order instead of first available
        return orderQueryDao.findImage(model)
                .map(bytes -> ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE)
                        .body(bytes))
                .orElse(ResponseEntity.notFound().build());
    }
}
