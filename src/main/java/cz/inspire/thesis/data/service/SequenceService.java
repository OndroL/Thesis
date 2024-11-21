package cz.inspire.thesis.data.service;

import cz.inspire.thesis.data.repository.SequenceRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SequenceService {

    @Inject
    private SequenceRepository sequenceRepository;


}
