package com.project.planner.participant.dto;

import java.util.UUID;

public record ParticipantResponseData(UUID id, String name, String email, Boolean isConfirmed) {
}
