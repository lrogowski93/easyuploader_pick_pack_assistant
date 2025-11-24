package com.demo.easyuploader_pick_pack_assistant.dto;

import lombok.Builder;

import java.time.Duration;
@Builder
public record GetUserStatsResponse(
        String login,
        int completedOrdersCount,
        Duration totalPackingTime,
        Duration averagePackingTime
) {
}
