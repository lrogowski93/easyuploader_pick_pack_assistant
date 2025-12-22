package com.demo.easyuploader_pick_pack_assistant.controller;

import com.demo.easyuploader_pick_pack_assistant.dto.GetUserDetailedStatsResponse;
import com.demo.easyuploader_pick_pack_assistant.model.Order;

public class UserStatsDtoMapper {
    public static GetUserDetailedStatsResponse mapToDetailedStats(Order order, Long userId, String login) {
        return GetUserDetailedStatsResponse.builder()
                .userId(userId)
                .login(login)
                .orderId(order.getId())
                .pickPackStartTime(order.getPickPackStartTime())
                .completionTime(order.getCompletionTime())
                .largeSizeOrder(order.isLargeSizeOrder())
                .build();
    }
}
