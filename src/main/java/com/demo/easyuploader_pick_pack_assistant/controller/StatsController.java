package com.demo.easyuploader_pick_pack_assistant.controller;

import com.demo.easyuploader_pick_pack_assistant.dto.GetUserStatsResponse;
import com.demo.easyuploader_pick_pack_assistant.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @GetMapping("/stats/{userId}")
    public ResponseEntity<GetUserStatsResponse> getUserStats(@PathVariable Long userId,
                                                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(statsService.getUserStats(userId, startDate, endDate));
    }

    @GetMapping("/stats")
    public ResponseEntity<List<GetUserStatsResponse>> getAllUsersStats(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        List<GetUserStatsResponse> statsList = statsService.getAllUsersStats(startDate, endDate);
        return ResponseEntity.ok(statsList);
    }
}
