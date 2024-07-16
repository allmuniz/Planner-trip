package com.project.planner.participant.services;

import com.project.planner.participant.dto.ParticipantResponseCreate;
import com.project.planner.participant.dto.ParticipantResponseData;
import com.project.planner.participant.entities.Participant;
import com.project.planner.participant.repositories.ParticipantRepository;
import com.project.planner.trip.entities.Trip;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ParticipantService {

    private final ParticipantRepository participantRepository;

    public ParticipantService(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    public void registerParticipantsToEvent(List<String> participantsToInvite, Trip trip){
        List<Participant> participants = participantsToInvite.stream().map(email -> new Participant(email, trip)).toList();

        this.participantRepository.saveAll(participants);

        System.out.println(participants.getFirst().getId());
    }

    public ParticipantResponseCreate registerParticipantToEvent(String email, Trip trip){
        Participant newParticipant = new Participant(email, trip);
        this.participantRepository.save(newParticipant);

        return new ParticipantResponseCreate(newParticipant.getId());
    }

    public void triggerConfirmationEmailToParticipants(UUID tripId){}

    public void triggerConfirmationEmailToParticipant(String email){}

    public List<ParticipantResponseData> allParticipantsFromEvent(UUID tripId){
        return this.participantRepository.findByTripId(tripId).stream().map(participant ->
                new ParticipantResponseData(participant.getId(), participant.getName(), participant.getEmail(), participant.getIsConfirmed())
                ).toList();
    }

}
