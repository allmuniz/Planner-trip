package com.project.planner.trip.controllers;

import com.project.planner.activity.dto.ActivityRequestPayload;
import com.project.planner.activity.dto.ActivityResponse;
import com.project.planner.activity.dto.ActivityResponseData;
import com.project.planner.activity.services.ActivityService;
import com.project.planner.exceptions.IncorrectDateException;
import com.project.planner.link.dto.LinkRequestPayload;
import com.project.planner.link.dto.LinkResponse;
import com.project.planner.link.dto.LinkResponseData;
import com.project.planner.link.services.LinkService;
import com.project.planner.participant.dto.ParticipantRequestPayload;
import com.project.planner.participant.dto.ParticipantResponseCreate;
import com.project.planner.participant.dto.ParticipantResponseData;
import com.project.planner.participant.services.ParticipantService;
import com.project.planner.trip.dto.TripRequestPayload;
import com.project.planner.trip.dto.TripResponseCreate;
import com.project.planner.trip.entities.Trip;
import com.project.planner.trip.services.TripService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/trips")
@Tag(name = "Trip", description = "Gestão de viagens")
public class TripController {

    private final ParticipantService participantService;
    private final TripService tripService;
    private final ActivityService activityService;
    private final LinkService linkService;

    public TripController(ParticipantService participantService, TripService tripService, ActivityService activityService,
                          LinkService linkService) {
        this.participantService = participantService;
        this.tripService = tripService;
        this.activityService = activityService;
        this.linkService = linkService;
    }

    @PostMapping
    @Operation(description = "Insira os dados para criar uma viagem",
                summary = "Criação de viagem")
    public ResponseEntity<TripResponseCreate> createTrip(@RequestBody TripRequestPayload payload) {
        try {
            TripResponseCreate tripResponseCreate = this.tripService.registerTrip(payload);
            return ResponseEntity.ok(tripResponseCreate);
        } catch (IncorrectDateException e) {
            throw new IncorrectDateException("A data do fim deve ser depois da de inicio");
        }
    }

    @GetMapping("/{tripId}")
    @Operation(description = "Insira o id da viagem para obter o resultado",
            summary = "Detalhes da viagem")
    public ResponseEntity<Trip> tripDetails(@PathVariable UUID tripId) {
        return tripService.checkTripExists(tripId).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{tripId}")
    @Operation(description = "Informe o id da viagem e altere os valores disponíveis",
            summary = "Atualização da viagem")
    public ResponseEntity<TripResponseCreate> updateTrip(@PathVariable UUID tripId, @RequestBody TripRequestPayload payload) {
        if (tripService.checkTripExists(tripId).isPresent()) {
            TripResponseCreate tripResponseCreate = tripService.updateTrip(tripId,payload);
            return ResponseEntity.ok(tripResponseCreate);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{tripId}/confirm")
    @Operation(description = "Insira o id da viagem para confirmar o responsavel da viagem",
            summary = "Confirmação da viagem")
    public ResponseEntity<Trip> confirmTrip(@PathVariable UUID tripId) {
        if (tripService.checkTripExists(tripId).isPresent()) {
            Trip rawTrip = tripService.confirmTrip(tripId);
            this.participantService.triggerConfirmationEmailToParticipants(tripId);

            return ResponseEntity.ok(rawTrip);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{tripId}/invite")
    @Operation(description = "Insira o id da viagem para registrar os participantes a viagem",
            summary = "Convite para participantes")
    public ResponseEntity<ParticipantResponseCreate> inviteParticipant(@PathVariable UUID tripId, @RequestBody ParticipantRequestPayload payload) {
        if (tripService.checkTripExists(tripId).isPresent()) {
            Trip rawTrip = tripService.checkTripExists(tripId).get();
            ParticipantResponseCreate participantResponse = this.participantService.registerParticipantToEvent(payload.email(), rawTrip);

            if (rawTrip.getIsConfirmed()) this.participantService.triggerConfirmationEmailToParticipant(payload.email());

            return ResponseEntity.ok(participantResponse);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{tripId}/participants")
    @Operation(description = "Insira o id da viagem para obter todos os participantes da viagem",
            summary = "Lista de participantes")
    public ResponseEntity<List<ParticipantResponseData>> allParticipants(@PathVariable UUID tripId) {
        List<ParticipantResponseData> participantList = this.participantService.allParticipantsFromEvent(tripId);

        return ResponseEntity.ok(participantList);
    }

    @PostMapping("/{tripId}/activities")
    @Operation(description = "Insira o id da viagem e os dados para adicionar uma atividade",
            summary = "Criação das atividades")
    public ResponseEntity<ActivityResponse> registerActivity(@PathVariable UUID tripId, @RequestBody ActivityRequestPayload payload) {
        try {
            Trip rawTrip = tripService.checkTripExists(tripId).get();

            ActivityResponse activityResponse = this.activityService.registerActivity(payload, rawTrip);

            return ResponseEntity.ok(activityResponse);
        }catch (IncorrectDateException e) {
            throw new IncorrectDateException("A data deve ser entre a de inicio e fim");
        }
    }

    @GetMapping("/{tripId}/activities")
    @Operation(description = "Insira o id da viagem para obter a lista de atividades",
            summary = "Lista de atividades")
    public ResponseEntity<List<ActivityResponseData>> allActivities(@PathVariable UUID tripId) {
        List<ActivityResponseData> activitiesList = this.activityService.allActivitiesFromEvent(tripId);

        return ResponseEntity.ok(activitiesList);
    }

    @PostMapping("/{tripId}/links")
    @Operation(description = "Insira o id da viagem e os dados do link para adicionar a viagem",
            summary = "Adição de links")
    public ResponseEntity<LinkResponse> registerLink(@PathVariable UUID tripId, @RequestBody LinkRequestPayload payload) {
        if (tripService.checkTripExists(tripId).isPresent()) {
            Trip rawTrip = tripService.checkTripExists(tripId).get();

            LinkResponse linkResponse = this.linkService.registerLink(payload, rawTrip);

            return ResponseEntity.ok(linkResponse);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{tripId}/links")
    @Operation(description = "Insira o id da viagem para obter a lista de links",
            summary = "Lista de links")
    public ResponseEntity<List<LinkResponseData>> allLinks(@PathVariable UUID tripId) {
        List<LinkResponseData> linksList = this.linkService.allActivitiesFromEvent(tripId);

        return ResponseEntity.ok(linksList);
    }
}