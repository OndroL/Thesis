package cz.inspire.thesis.data.service;

import cz.inspire.thesis.data.dto.SequenceDetails;
import cz.inspire.thesis.data.model.SequenceEntity;
import cz.inspire.thesis.data.model.SkladSequenceEntity;
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

    /**
     * Creates a new Sequence entity.
     */
    public String ejbCreate(SequenceDetails details) throws CreateException {
        try {
            SequenceEntity entity = new SequenceEntity();
            if (details.getName() == null) {
                details.setName(generateGUID(entity));
            }
            setDetails(entity, details);
            sequenceRepository.save(entity);

            // Post-create logic
            ejbPostCreate(details, entity);

            return entity.getName();
        } catch (Exception e) {
            throw new CreateException("Failed to create Sequence entity", e);
        }
    }

    public void ejbPostCreate(SequenceDetails details, SequenceEntity entity) throws CreateException {
        if (details.getStornoSeqName() != null) {
            Optional<SkladSequenceEntity> stornoSeq = sequenceRepository.findById(details.getStornoSeqName());
            if (stornoSeq.isPresent()) {
                entity.setStornoSeq(stornoSeq.get());
                sequenceRepository.save(entity);
            } else {
                throw new CreateException("Storno sequence not found for name: " + details.getStornoSeqName());
            }
        }
    }

    /**
     * Sets the details of a Sequence entity.
     */
    public void setDetails(SequenceEntity entity, SequenceDetails details) {
        entity.setPattern(details.getPattern());
        entity.setMinvalue(details.getMinValue());
        entity.setLast(details.getLast());
        entity.setType(details.getType());
    }

    /**
     * Retrieves details of a Sequence entity.
     */
    public SequenceDetails getDetails(SequenceEntity entity) {
        SequenceDetails details = new SequenceDetails();
        details.setName(entity.getName());
        details.setPattern(entity.getPattern());
        details.setMinValue(entity.getMinvalue());
        details.setLast(entity.getLast());
        details.setType(entity.getType());
        if (entity.getStornoSeq() != null) {
            /**
             * TODO: Ask how this magic with sequences works !
             * This was here before :
             * details.setStornoSeqName(entity.getStornoSeq().getName());
             */
            details.setStornoSeqName(entity.getStornoSeq().getSekvence());
        }
        return details;
    }

    /**
     *TODO: FIX this, plus ask what is SequencePattern class !
     */
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
//        try {
//            pattern.parse(entity.getLast());
//        } catch (ParseException e) {
//            return preInit(entity, time);
//        }
//        if (increment) {
//            int serial = pattern.getSerial() + 1;
//            pattern.setSerial(serial);
//            entity.setLast(pattern.format());
//            sequenceRepository.save(entity);
//        }
//        return pattern.format();
//    }
}
