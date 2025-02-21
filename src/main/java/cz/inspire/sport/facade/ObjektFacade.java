package cz.inspire.sport.facade;

import cz.inspire.sport.service.ObjektService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ObjektFacade {

    @Inject
    ObjektService objektService;

}
