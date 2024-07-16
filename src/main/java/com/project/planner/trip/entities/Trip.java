package com.project.planner.trip.entities;

import com.project.planner.trip.dto.TripRequestPayload;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Table(name = "trips")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String destination;

    @Column(name = "starts_at", nullable = false)
    private LocalDateTime startsAt;

    @Column(name = "ends_at", nullable = false)
    private LocalDateTime endsAt;

    @Column(name = "is_confirmed", nullable = false)
    private Boolean isConfirmed;

    @Column(name = "owner_name", nullable = false)
    private String ownerName;

    @Column(name = "owner_email", nullable = false)
    private String ownerEmail;

    public Trip(String destination, String startsAt, String endsAt, String ownerName, String ownerEmail) {
        this.destination = destination;
        this.startsAt = LocalDateTime.parse(startsAt, DateTimeFormatter.ISO_DATE_TIME);
        this.endsAt = LocalDateTime.parse(endsAt, DateTimeFormatter.ISO_DATE_TIME);
        this.isConfirmed = false;
        this.ownerName = ownerName;
        this.ownerEmail = ownerEmail;
    }
}
