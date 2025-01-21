package cz.inspire.common.service;

import cz.inspire.common.entity.NastaveniEntity;
import cz.inspire.common.repository.NastaveniRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class NastaveniService extends BaseService<NastaveniEntity, String, NastaveniRepository> {

    public NastaveniService() {
    }

    @Inject
    public NastaveniService(NastaveniRepository nastaveniRepository) {
        super(nastaveniRepository);
    }

}