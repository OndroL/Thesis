package cz.inspire.thesis.data.service.sport.objekt;

import cz.inspire.thesis.data.dto.sport.objekt.OvladacObjektuDetails;
import cz.inspire.thesis.data.model.sport.objekt.OvladacObjektuEntity;
import cz.inspire.thesis.data.repository.sport.objekt.OvladacObjektuRepository;
import cz.inspire.thesis.exceptions.CreateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static cz.inspire.thesis.data.utils.guidGenerator.generateGUID;

@ApplicationScoped
public class OvladacObjektuService {

    @Inject
    private OvladacObjektuRepository repository;

    public String ejbCreate(OvladacObjektuDetails details) throws CreateException {
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

    public Collection<OvladacObjektuDetails> findAll() {
        return repository.findAll().stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public Collection<OvladacObjektuDetails> findWithOvladacObjektu(String idOvladace) {
        return repository.findWithOvladacObjektu(idOvladace).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public Collection<OvladacObjektuDetails> findByObjekt(String objektId) {
        return repository.findByObjekt(objektId).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
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
