package cz.inspire.common.service;

import cz.inspire.common.entity.NastaveniJsonEntity;
import cz.inspire.common.repository.NastaveniJsonRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class NastaveniJsonService extends BaseService<NastaveniJsonEntity, String, NastaveniJsonRepository> {

    public NastaveniJsonService() {
    }
    
    @Inject
    public NastaveniJsonService(NastaveniJsonRepository nastaveniJsonRepository) {
        super(nastaveniJsonRepository);
    }
}
