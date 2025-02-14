package cz.inspire.sport.service;

import cz.inspire.common.service.BaseService;
import cz.inspire.sport.entity.OmezeniRezervaciEntity;
import cz.inspire.sport.repository.OmezeniRezervaciRepository;
import jakarta.inject.Inject;

public class OmezeniRezervaciService extends BaseService<OmezeniRezervaciEntity, String, OmezeniRezervaciRepository> {
    public OmezeniRezervaciService() {
    }

    @Inject
    public OmezeniRezervaciService(OmezeniRezervaciRepository repository) {
        super(repository);
    }

}
