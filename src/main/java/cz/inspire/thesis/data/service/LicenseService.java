package cz.inspire.thesis.data.service;

import cz.inspire.thesis.data.dto.LicenseDetails;
import cz.inspire.thesis.data.model.LicenseEntity;
import cz.inspire.thesis.data.repository.LicenseRepository;
import cz.inspire.thesis.data.utils.guidGenerator;
import cz.inspire.thesis.exceptions.CreateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


import java.util.Date;

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
        details.setCenterId(entity.getCenterId());
        details.setCustomer(entity.getCustomer());
        details.setValid(entity.getValid());
        details.setCenterOnline(entity.getCenterOnline());
        details.setValidFrom(entity.getValidFrom());
        details.setValidFromSet(entity.getValidFromSet());
        details.setValidTo(entity.getValidTo());
        details.setActivityLimit(entity.getActivityLimit());
        details.setSportCenterLimit(entity.getSportCenterLimit());
        details.setSportCustomersLimit(entity.getSportCustomersLimit());
        details.setUsersLimit(entity.getUsersLimit());
        details.setCustomerGroupsLimit(entity.getCustomerGroupsLimit());
        details.setPokladnaLimit(entity.getPokladnaLimit());
        details.setSkladLimit(entity.getSkladLimit());
        details.setOvladaniQuido(entity.getOvladaniQuido());
        details.setModules(entity.getModules());
        details.setMaxClients(entity.getMaxClients());
        details.setHash(entity.getHash());
        details.setCreatedDate(entity.getCreatedDate());
        details.setGeneratedDate(entity.getGeneratedDate());

        return details;
    }

    /**
     * Same difference as above here
     */
    public void setDetails(LicenseEntity entity, LicenseDetails details) {
        entity.setCenterId(details.getCenterId());
        entity.setCustomer(details.getCustomer());
        entity.setValid(details.getValid());
        entity.setCenterOnline(details.getCenterOnline());
        entity.setValidFrom(details.getValidFrom());
        entity.setValidFromSet(details.getValidFromSet());
        entity.setValidTo(details.getValidTo());
        entity.setActivityLimit(details.getActivityLimit());
        entity.setSportCenterLimit(details.getSportCenterLimit());
        entity.setSportCustomersLimit(details.getSportCustomersLimit());
        entity.setUsersLimit(details.getUsersLimit());
        entity.setCustomerGroupsLimit(details.getCustomerGroupsLimit());
        entity.setPokladnaLimit(details.getPokladnaLimit());
        entity.setSkladLimit(details.getSkladLimit());
        entity.setOvladaniQuido(details.getOvladaniQuido());
        entity.setModules(details.getModules());
        entity.setMaxClients(details.getMaxClients());
        entity.setHash(details.getHash());
        entity.setGeneratedDate(details.getGeneratedDate());
    }

    public String ejbCreate(LicenseDetails details) throws CreateException {
        try {
            LicenseEntity entity = new LicenseEntity();

            if (details.getId() == null) {
                details.setId(guidGenerator.generateGUID(entity));
            }
            entity.setId(details.getId());
            entity.setCreatedDate(new Date());
            setDetails(entity, details);

            licenseRepository.save(entity);

            return entity.getId();

        } catch (Exception e) {
            throw new CreateException("Failed to create License entity", e);
        }
    }
}
