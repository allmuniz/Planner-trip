package com.project.planner.participant.controllers;

import com.project.planner.participant.dto.ParticipantRequestPayload;
import com.project.planner.participant.entities.Participant;
import com.project.planner.participant.repositories.ParticipantRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/participants")
@Tag(name = "Participant", description = "Controle de participantes")
public class ParticipantController {

    private final ParticipantRepository participantRepository;

    public ParticipantController(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    @PostMapping("/{id}/confirm")
    @Operation(description = "Insira o id do participante e os dados solicitados para confirmar",
            summary = "Confirmação do participante")
    public ResponseEntity<Participant> confirmParticipant(@PathVariable UUID id, @RequestBody ParticipantRequestPayload payload) {
        Optional<Participant> participant = participantRepository.findById(id);

        if (participant.isPresent()) {
            Participant rawParticipant = participant.get();
            rawParticipant.setName(payload.name());
            rawParticipant.setIsConfirmed(true);

            participantRepository.save(rawParticipant);
            return ResponseEntity.ok(rawParticipant);
        }
        return ResponseEntity.notFound().build();
    }
}
