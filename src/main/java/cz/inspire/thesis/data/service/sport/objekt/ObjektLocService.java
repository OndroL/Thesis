package cz.inspire.thesis.data.service.sport.objekt;

import cz.inspire.thesis.data.dto.sport.objekt.ObjektLocDetails;
import cz.inspire.thesis.data.model.sport.objekt.ObjektLocEntity;
import cz.inspire.thesis.data.repository.sport.objekt.ObjektLocRepository;
import cz.inspire.thesis.exceptions.CreateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Collection;
import java.util.stream.Collectors;

import static cz.inspire.thesis.data.utils.guidGenerator.generateGUID;

@ApplicationScoped
public class ObjektLocService {

    @Inject
    private ObjektLocRepository objektLocRepository;

    public String ejbCreate(ObjektLocDetails details) throws CreateException {
        try {
            ObjektLocEntity entity = new ObjektLocEntity();
            if (details.getId() == null) {
                details.setId(generateGUID(entity));
            }
            entity.setId(details.getId());
            entity.setJazyk(details.getJazyk());
            entity.setNazev(details.getNazev());
            entity.setPopis(details.getPopis());
            entity.setZkracenyNazev(details.getZkracenyNazev());

            objektLocRepository.save(entity);

            return entity.getId();
        } catch (Exception e) {
            throw new CreateException("Failed to create ObjektLoc entity", e);
        }
    }

    public ObjektLocDetails getDetails(ObjektLocEntity entity) {
        return new ObjektLocDetails(
                entity.getId(),
                entity.getJazyk(),
                entity.getNazev(),
                entity.getPopis(),
                entity.getZkracenyNazev()
        );
    }

    public Collection<ObjektLocDetails> findAll() {
        return objektLocRepository.findAll().stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public ObjektLocDetails findById(String id) {
        return objektLocRepository.findOptionalBy(id)
                .map(this::getDetails)
                .orElseThrow(() -> new IllegalArgumentException("ObjektLoc not found with id: " + id));
    }
}
