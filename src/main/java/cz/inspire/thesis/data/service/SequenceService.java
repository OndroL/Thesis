package cz.inspire.thesis.data.service;

import cz.inspire.thesis.data.dto.SequenceDetails;
import cz.inspire.thesis.data.model.SequenceEntity;
import cz.inspire.thesis.data.repository.SequenceRepository;
import cz.inspire.thesis.exceptions.CreateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

import static cz.inspire.thesis.data.utils.guidGenerator.generateGUID;

@ApplicationScoped
public class SequenceService {

    @Inject
    private SequenceRepository sequenceRepository;

    public String ejbCreate(SequenceDetails details) throws CreateException {
        try {
            SequenceEntity entity = new SequenceEntity();
            if (details.getName() == null) {
                details.setName(generateGUID(entity));
            }
            setDetails(entity, details);
            sequenceRepository.save(entity);

            if (details.getStornoSeqName() != null) {
                Optional<SequenceEntity> stornoSeq = sequenceRepository.findById(details.getStornoSeqName());
                if (stornoSeq.isPresent()) {
                    entity.setStornoSeq(stornoSeq.get());
                    sequenceRepository.save(entity);
                } else {
                    throw new CreateException("Storno sequence not found for name: " + details.getStornoSeqName());
                }
            }

            return entity.getName();
        } catch (Exception e) {
            throw new CreateException("Failed to create Sequence entity", e);
        }
    }

    public void setDetails(SequenceEntity entity, SequenceDetails details) {
        entity.setPattern(details.getPattern());
        entity.setMinValue(details.getMinValue());
        entity.setLast(details.getLast());
        entity.setType(details.getType());
    }

    public SequenceDetails getDetails(SequenceEntity entity) {
        SequenceDetails details = new SequenceDetails();
        details.setName(entity.getName());
        details.setPattern(entity.getPattern());
        details.setMinValue(entity.getMinValue());
        details.setLast(entity.getLast());
        details.setType(entity.getType());
        if (entity.getStornoSeq() != null) {
            details.setStornoSeqName(entity.getStornoSeq().getName());
        }
        return details;
    }
// Here is business logic from Bean
// TODO: complete this when you get SequencePattern entity;
//
//    public void init(SequenceEntity entity, Date time) {
//        String last = preInit(entity, time);
//        entity.setLast(last);
//        sequenceRepository.save(entity);
//    }
//
//    private String preInit(SequenceEntity entity, Date time) {
//        SequencePattern pattern = new SequencePattern(entity.getPattern());
//        pattern.setSerial(entity.getMinvalue());
//        pattern.setTime(time);
//        return pattern.format();
//    }
//
//    public String preview(SequenceEntity entity) {
//        return processNext(entity, new Date(), false);
//    }
//
//    public String next(SequenceEntity entity, Date time) {
//        return processNext(entity, time, true);
//    }
//
//    private String processNext(SequenceEntity entity, Date time, boolean increment) {
//        SequencePattern pattern = new SequencePattern(entity.getPattern());
//        pattern.setTime(time);
//
//        try {
//            pattern.parse(entity.getLast());
//        } catch (ParseException e) {
//            return preInit(entity, time);
//        }
//
//        if (increment) {
//            int serial = pattern.getSerial() + 1;
//            pattern.setSerial(serial);
//            entity.setLast(pattern.format());
//            sequenceRepository.save(entity);
//        }
//        return pattern.format();
//    }
//
//    public void undo(SequenceEntity entity, Date time, String actual) {
//        if (entity.getLast() == null || !entity.getLast().equals(actual)) {
//            return;
//        }
//
//        SequencePattern pattern = new SequencePattern(entity.getPattern());
//        pattern.setTime(time);
//
//        try {
//            pattern.parse(entity.getLast());
//        } catch (ParseException e) {
//            return;
//        }
//
//        int serial = pattern.getSerial() - 1;
//        pattern.setSerial(serial);
//        entity.setLast(pattern.format());
//        sequenceRepository.save(entity);
//    }
}

