package com.project.planner.activity.services;

import com.project.planner.activity.dto.ActivityRequestPayload;
import com.project.planner.activity.dto.ActivityResponse;
import com.project.planner.activity.dto.ActivityResponseData;
import com.project.planner.activity.entities.Activity;
import com.project.planner.activity.repositories.ActivityRepository;
import com.project.planner.exceptions.IncorrectDateException;
import com.project.planner.trip.entities.Trip;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;

    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public ActivityResponse registerActivity(ActivityRequestPayload payload, Trip trip) {
        LocalDateTime occurs_at = LocalDateTime.parse(payload.occurs_at(), DateTimeFormatter.ISO_DATE_TIME);
        try {
            if (trip.getStartsAt().isBefore(occurs_at)) {
                if (trip.getEndsAt().isAfter(occurs_at)) {
                    Activity newActivity = new Activity(payload.title(), payload.occurs_at(), trip);
                    activityRepository.save(newActivity);

                    return new ActivityResponse(newActivity.getId());
                }else {
                    throw new IncorrectDateException("A data deve ser entre a de inicio e fim");
                }
            } else {
                throw new IncorrectDateException("A data deve ser entre a de inicio e fim");
            }
        } catch (IncorrectDateException e) {
            throw new IncorrectDateException(e.getMessage());
        }
    }

    public List<ActivityResponseData> allActivitiesFromEvent(UUID tripId){
        return this.activityRepository.findByTripId(tripId).stream().map(activity ->
                new ActivityResponseData(activity.getId(), activity.getTitle(), activity.getOccursAt())
        ).toList();
    }
}
