package cz.inspire.thesis.data.service.sport.objekt;

import cz.inspire.thesis.data.dto.sport.objekt.OvladacObjektuDetails;
import cz.inspire.thesis.data.model.sport.objekt.OvladacObjektuEntity;
import cz.inspire.thesis.data.repository.sport.objekt.OvladacObjektuRepository;
import cz.inspire.thesis.exceptions.ApplicationException;
import cz.inspire.thesis.exceptions.CreateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.deltaspike.jpa.api.transaction.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cz.inspire.thesis.data.utils.guidGenerator.generateGUID;

@ApplicationScoped
public class OvladacObjektuService {

    @Inject
    private OvladacObjektuRepository repository;

    @Transactional
    public String create(OvladacObjektuDetails details) throws CreateException {
        try {
            OvladacObjektuEntity entity = new OvladacObjektuEntity();
            if (details.getId() == null) {
                details.setId(generateGUID(entity));
            }
            entity.setId(details.getId());
            entity.setIdOvladace(details.getIdOvladace());
            entity.setManual(details.getManual());
            entity.setAutomat(details.getAutomat());
            entity.setDelkaSepnutiPoKonci(details.getDelkaSepnutiPoKonci());
            entity.setZapnutiPredZacatkem(details.getZapnutiPredZacatkem());
            entity.setCislaZapojeni(encodeNumbersToString(details.getCislaZapojeniList()));
            entity.setObjektId(details.getObjektId());

            repository.save(entity);

            return entity.getId();
        } catch (Exception e) {
            throw new CreateException("Failed to create OvladacObjektu entity", e);
        }
    }

    @Transactional
    public void remove(String id) throws ApplicationException {
        try {
            OvladacObjektuEntity entity = repository.findOptionalBy(id)
                    .orElseThrow(() -> new ApplicationException("OvladacObjektu entity not found with id : " + id));

            repository.remove(entity);
        } catch (Exception e) {
            throw new ApplicationException("Failed to remove OvladacObjektu entity", e);
        }
    }

    @Transactional
    public void setDetails(OvladacObjektuDetails details) throws ApplicationException {

        try {
            OvladacObjektuEntity entity = repository.findOptionalBy(details.getId())
                    .orElseThrow(() -> new ApplicationException("OvladacObjektu entity not found with id : " + details.getId()));
            entity.setIdOvladace(details.getIdOvladace());
            entity.setCislaZapojeni(encodeNumbersToString(details.getCislaZapojeniList()));
            entity.setAutomat(details.getAutomat());
            entity.setManual(details.getManual());
            entity.setDelkaSepnutiPoKonci(details.getDelkaSepnutiPoKonci());
            entity.setZapnutiPredZacatkem(details.getZapnutiPredZacatkem());
            // This is kinda redundant
            // In old Bean -> "setObjektId(getObjektId());"
            entity.setObjektId(entity.getObjektId());

            repository.save(entity);

        } catch (Exception e) {
            throw  new ApplicationException("Failed to update OvladacObjektu entity");
        }
    }

    public OvladacObjektuDetails getDetails(OvladacObjektuEntity entity) {
        return new OvladacObjektuDetails(
                entity.getId(),
                entity.getIdOvladace(),
                entity.getManual(),
                entity.getAutomat(),
                entity.getDelkaSepnutiPoKonci(),
                entity.getZapnutiPredZacatkem(),
                decodeNumbersFromString(entity.getCislaZapojeni()),
                entity.getObjektId()
        );
    }



    public Collection<OvladacObjektuEntity> findAll() {
        return repository.findAll();
    }

    public Collection<OvladacObjektuEntity> findWithOvladacObjektu(String idOvladace) {
        return repository.findWithOvladacObjektu(idOvladace);
    }



    public Collection<OvladacObjektuEntity> findByObjekt(String objektId) {
        return repository.findByObjekt(objektId);
    }

    public Optional<OvladacObjektuEntity> findOptionalBy (String objektId) {
        return repository.findOptionalBy(objektId);
    }

    /** These functions are generated to mimic functionality of
     * OvladacObjektuBaseUtil.encodeNumbersToString()
    */
    private String encodeNumbersToString(List<Integer> numbers) {
        if (numbers == null) return null;
        return numbers.stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    private List<Integer> decodeNumbersFromString(String numbers) {
        if (numbers == null || numbers.isEmpty()) return List.of();
        return List.of(numbers.split(",")).stream().map(Integer::valueOf).collect(Collectors.toList());
    }
}
