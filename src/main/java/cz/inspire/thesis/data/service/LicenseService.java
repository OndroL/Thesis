package cz.inspire.thesis.data.service;

import cz.inspire.thesis.data.dto.LicenseDetails;
import cz.inspire.thesis.data.model.LicenseEntity;
import cz.inspire.thesis.data.repository.LicenseRepository;
import cz.inspire.thesis.data.utils.LicenseUtil;
import cz.inspire.thesis.exceptions.CreateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


import java.util.Date;

@ApplicationScoped
public class LicenseService {

    @Inject
    private LicenseRepository licenseRepository;

    /**
     * Here is a difference from Bean implementation
     * In Bean implementation this is part of Bean so it has direct access to class hence getDetails()
     * But it being implemented in Service layer, it needs reference to entity
     */
    public LicenseDetails getDetails(LicenseEntity entity) {
        LicenseDetails details = new LicenseDetails();
        details.setId(entity.getId());
        details.setCenterId(entity.getCenterid());
        details.setCustomer(entity.getCustomer());
        details.setValid(entity.getValid());
        details.setCenterOnline(entity.getCenteronline());
        details.setValidFrom(entity.getValidfrom());
        details.setValidFromSet(entity.getValidfromset());
        details.setValidTo(entity.getValidto());
        details.setActivityLimit(entity.getActivitylimit());
        details.setSportCenterLimit(entity.getSportcenterlimit());
        details.setSportCustomersLimit(entity.getSportcustomerslimit());
        details.setUsersLimit(entity.getUserslimit());
        details.setCustomerGroupsLimit(entity.getCustomergroupslimit());
        details.setPokladnaLimit(entity.getPokladnalimit());
        details.setSkladLimit(entity.getSkladlimit());
        details.setOvladaniQuido(entity.getOvladaniquido());
        details.setModules(entity.getModules());
        details.setMaxClients(entity.getMaxclients());
        details.setHash(entity.getHash());
        details.setCreatedDate(entity.getCreateddate());
        details.setGeneratedDate(entity.getGenerateddate());
        return details;
    }

    /**
     * Same difference as above here
     */
    public void setDetails(LicenseEntity entity, LicenseDetails details) {
        entity.setCenterid(details.getCenterId());
        entity.setCustomer(details.getCustomer());
        entity.setValid(details.getValid());
        entity.setCenteronline(details.getCenterOnline());
        entity.setValidfrom(details.getValidFrom());
        entity.setValidfromset(details.getValidFromSet());
        entity.setValidto(details.getValidTo());
        entity.setActivitylimit(details.getActivityLimit());
        entity.setSportcenterlimit(details.getSportCenterLimit());
        entity.setSportcustomerslimit(details.getSportCustomersLimit());
        entity.setUserslimit(details.getUsersLimit());
        entity.setCustomergroupslimit(details.getCustomerGroupsLimit());
        entity.setPokladnalimit(details.getPokladnaLimit());
        entity.setSkladlimit(details.getSkladLimit());
        entity.setOvladaniquido(details.getOvladaniQuido());
        entity.setModules(details.getModules());
        entity.setMaxclients(details.getMaxClients());
        entity.setHash(details.getHash());
        entity.setGenerateddate(details.getGeneratedDate());
    }

    public String ejbCreate(LicenseDetails details) throws CreateException {
        try {
            LicenseEntity entity = new LicenseEntity();

            if (details.getId() == null) {
                details.setId(LicenseUtil.generateGUID(entity));
            }
            entity.setId(details.getId());
            entity.setCreateddate(new Date());
            setDetails(entity, details);

            licenseRepository.save(entity);
            return entity.getId();
        } catch (Exception e) {
            throw new CreateException("Failed to create License entity", e);
        }
    }
}
