package com.project.planner.link.dto;

import java.util.UUID;

public record LinkResponseData(UUID linkId, String title, String url) {
}
