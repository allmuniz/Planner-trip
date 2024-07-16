package com.project.planner.activity.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ActivityResponseData(UUID activityId, String title, LocalDateTime occurs_at) {
}
