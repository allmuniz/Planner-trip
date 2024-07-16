package com.project.planner.trip.services;

import com.project.planner.exceptions.IncorrectDateException;
import com.project.planner.trip.dto.TripRequestPayload;
import com.project.planner.trip.dto.TripResponseCreate;
import com.project.planner.trip.entities.Trip;
import com.project.planner.trip.repositories.TripRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

@Service
public class TripService {

    private final TripRepository tripRepository;

    public TripService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public Optional<Trip> checkTripExists(UUID tripId) {
        return tripRepository.findById(tripId);
    }

    public TripResponseCreate registerTrip(TripRequestPayload payload) {
        LocalDateTime startsAt = LocalDateTime.parse(payload.starts_at(), DateTimeFormatter.ISO_DATE_TIME);
        LocalDateTime endsAt = LocalDateTime.parse(payload.ends_at(), DateTimeFormatter.ISO_DATE_TIME);
        if (startsAt.isBefore(endsAt)) {
            Trip newTrip = new Trip(payload.destination(), payload.starts_at(),payload.ends_at(),payload.owner_name(),payload.owner_email());
            tripRepository.save(newTrip);

            return new TripResponseCreate(newTrip.getId());
        }else {
            throw new IncorrectDateException("A data do fim deve ser depois da de inicio");
        }
    }

    public TripResponseCreate updateTrip(UUID tripId, TripRequestPayload payload) {
        if (checkTripExists(tripId).isPresent()){
            Trip rawtrip = checkTripExists(tripId).get();
            rawtrip.setDestination(payload.destination());
            rawtrip.setStartsAt(LocalDateTime.parse(payload.starts_at(), DateTimeFormatter.ISO_DATE_TIME));
            rawtrip.setEndsAt(LocalDateTime.parse(payload.ends_at(), DateTimeFormatter.ISO_DATE_TIME));

            this.tripRepository.save(rawtrip);
            return new TripResponseCreate(rawtrip.getId());
        }
        return null;
    }

    public Trip confirmTrip(UUID tripId) {
        if (checkTripExists(tripId).isPresent()){
            Trip rawtrip = checkTripExists(tripId).get();
            rawtrip.setIsConfirmed(true);

            this.tripRepository.save(rawtrip);
            return rawtrip;
        }
        return null;
    }
}
