package cz.inspire.thesis.data.repository.uzivatel;

import cz.inspire.thesis.data.model.uzivatel.OAuth2ClientSettingEntity;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

import java.util.List;

@Repository
public interface OAuth2ClientSettingRepository extends EntityRepository<OAuth2ClientSettingEntity,String> {

    List<OAuth2ClientSettingEntity> findAll();
}
