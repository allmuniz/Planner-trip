package com.project.planner.link.services;

import com.project.planner.link.dto.LinkRequestPayload;
import com.project.planner.link.dto.LinkResponse;
import com.project.planner.link.dto.LinkResponseData;
import com.project.planner.link.entities.Link;
import com.project.planner.link.repositries.LinkRepository;
import com.project.planner.trip.entities.Trip;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LinkService {

    private final LinkRepository linkRepository;

    public LinkService(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    public LinkResponse registerLink(LinkRequestPayload payload, Trip trip) {
        Link newLink = new Link(payload.title(), payload.url(), trip);
        linkRepository.save(newLink);

        return new LinkResponse(newLink.getId());
    }

    public List<LinkResponseData> allActivitiesFromEvent(UUID tripId){
        return this.linkRepository.findByTripId(tripId).stream().map(link ->
                new LinkResponseData(link.getId(), link.getTitle(), link.getUrl())
        ).toList();
    }
}
