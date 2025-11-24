package com.demo.easyuploader_pick_pack_assistant.service;

import com.demo.easyuploader_pick_pack_assistant.dto.GetUserStatsResponse;
import com.demo.easyuploader_pick_pack_assistant.model.Order;
import com.demo.easyuploader_pick_pack_assistant.model.User;
import com.demo.easyuploader_pick_pack_assistant.repository.jpa.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final OrderRepository orderRepository;
    private final UserService userService;

    public GetUserStatsResponse getUserStats(Long userId, LocalDateTime startDate, LocalDateTime endDate) {

        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date (" + startDate + ") has to be before end date (" + endDate + ").");
        }

        List<Order> completedOrders = orderRepository.findAllByPickPackerIdAndIsCompletedTrueAndCompletionTimeBetween(
                userId,
                startDate,
                endDate
        );

        int completedOrdersCount = completedOrders.size();


        Duration totalPackingDuration = completedOrders.stream()
                .map(order -> Duration.between(order.getPickPackStartTime(), order.getCompletionTime()))
                .reduce(Duration.ZERO, Duration::plus);

        Duration averagePackingDuration = Duration.ZERO;

        if (completedOrdersCount > 0) {
            long totalNanos = totalPackingDuration.toNanos();
            long averageNanos = totalNanos / completedOrdersCount;
            averagePackingDuration = Duration.ofNanos(averageNanos);
        }

        return  GetUserStatsResponse.builder()
                .login(userService.getUserLoginById(userId))
                .completedOrdersCount(completedOrdersCount)
                .totalPackingTime(totalPackingDuration)
                .averagePackingTime(averagePackingDuration)
                .build();

    }

    public List<GetUserStatsResponse> getAllUsersStats(LocalDateTime startDate, LocalDateTime endDate) {

        List<User> allPickPackers = userService.findAllPickPackers();


        return allPickPackers.stream()

                .map(user -> {
                        return getUserStats(user.getId(), startDate, endDate);
                })
                .collect(Collectors.toList());
    }

    }

