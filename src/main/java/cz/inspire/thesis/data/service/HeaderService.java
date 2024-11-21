package cz.inspire.thesis.data.service;

import cz.inspire.thesis.data.model.HeaderEntity;
import cz.inspire.thesis.data.repository.HeaderRepository;
import cz.inspire.thesis.exceptions.CreateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;

import java.util.List;

/**
 * This is only a concept of how Service layer could look like
 * names and return types of functions can a should change
 * -- but it all depend on what other layers of App expect
 * TODO: Add CRUD operations ?
 */

@ApplicationScoped
public class HeaderService {
    @Inject
    private HeaderRepository headerRepository;

    /**
     * Personally I would include ejbPostCreate functionality in ejbCreate
     * or whatever function tasked with creating entity
     */
    public String ejbCreate(String id, int field, int location) throws CreateException {
        HeaderEntity headerEntity = new HeaderEntity();
        headerEntity.setId(id);
        headerEntity.setField(field);
        headerEntity.setLocation(location);
        try {
            headerRepository.save(headerEntity);
            /// Here would be ejbPostCreate logic.
        } catch (PersistenceException e) {
            throw new CreateException("Failed to save entity", e);
        }
        return id;
    }

    /**
     *  For example, most common approach is this :
     *  public Header save(int field, int location) {
     *         Header header = new Header();
     *         header.setField(field);
     *         header.setLocation(location);
     *
     *         try {
     *             headerRepository.save(header);
     *         } catch (PersistenceException e) {
     *             throw new CreateException("Failed to save entity", e);
     *         }
     *
     *         return headerRepository.save(header);
     *     }
     */

    public List<HeaderEntity> findValidAtributes()
    {
        return headerRepository.findValidAtributes();
    }
}
