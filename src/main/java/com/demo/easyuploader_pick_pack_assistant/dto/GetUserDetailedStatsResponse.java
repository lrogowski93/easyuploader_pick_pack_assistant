package com.demo.easyuploader_pick_pack_assistant.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record GetUserDetailedStatsResponse(
        Long userId,
        String login,
        Long orderId,
        LocalDateTime pickPackStartTime,
        LocalDateTime completionTime,
        boolean largeSizeOrder
) {
}
