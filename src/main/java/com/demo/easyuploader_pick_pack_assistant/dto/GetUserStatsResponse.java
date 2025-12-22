package com.demo.easyuploader_pick_pack_assistant.dto;

import lombok.Builder;

import java.time.Duration;
@Builder
public record GetUserStatsResponse(
        Long userId,
        String login,
        int completedOrdersCount,
        Duration totalPackingTime,
        Duration averagePackingTime
) {
}
