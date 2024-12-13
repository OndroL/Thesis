package cz.inspire.thesis.data.service.sport.objekt;

import cz.inspire.thesis.data.dto.sport.objekt.OmezeniRezervaciDetails;
import cz.inspire.thesis.data.model.sport.objekt.OmezeniRezervaciEntity;
import cz.inspire.thesis.data.repository.sport.objekt.OmezeniRezervaciRepository;
import cz.inspire.thesis.exceptions.CreateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Collection;
import java.util.stream.Collectors;

import static cz.inspire.thesis.data.utils.guidGenerator.generateGUID;

@ApplicationScoped
public class OmezeniRezervaciService {

    @Inject
    private OmezeniRezervaciRepository repository;

    public String ejbCreate(OmezeniRezervaciDetails details) throws CreateException {
        try {
            OmezeniRezervaciEntity entity = new OmezeniRezervaciEntity();
            if (details.getObjektId() == null) {
                details.setObjektId(generateGUID(entity));
            }
            entity.setObjektId(details.getObjektId());
            entity.setOmezeni(details.getOmezeni());

            repository.save(entity);

            return entity.getObjektId();
        } catch (Exception e) {
            throw new CreateException("Failed to create OmezeniRezervaci entity", e);
        }
    }

    public OmezeniRezervaciDetails getDetails(OmezeniRezervaciEntity entity) {
        return new OmezeniRezervaciDetails(
                entity.getObjektId(),
                entity.getOmezeni()
        );
    }

    public Collection<OmezeniRezervaciDetails> findAll() {
        return repository.findAll().stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }
}
