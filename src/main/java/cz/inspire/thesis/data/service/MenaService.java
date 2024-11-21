package cz.inspire.thesis.data.service;

import cz.inspire.thesis.data.dto.MenaDetails;
import cz.inspire.thesis.data.model.MenaEntity;
import cz.inspire.thesis.data.repository.MenaRepository;
import cz.inspire.thesis.exceptions.CreateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/// TODO : REMOVE comments

/**
 * This is only a concept of how Service layer could look like
 * names and return types of functions can and should change
 * -- but it all depend on what other layers of App expect
 * TODO: Add CRUD operations ?
 */

@ApplicationScoped
public class MenaService {

    @Inject
    private MenaRepository menaRepository;

    private static final String VYCETKA_DELIM = ";";


    public MenaDetails getDetails(MenaEntity menaEntity) {
        MenaDetails details = new MenaDetails();
        details.setId(menaEntity.getId());
        details.setKod(menaEntity.getKod());
        details.setKodNum(menaEntity.getKodnum());
        details.setVycetkaList(parseVycetkaList(menaEntity.getVycetka()));
        details.setZaokrouhleniHotovost(menaEntity.getZaokrouhlenihotovost());
        details.setZaokrouhleniKarta(menaEntity.getZaokrouhlenikarta());
        return details;
    }

    public String ejbCreate(MenaDetails menaDetails) throws CreateException {
        try {
            MenaEntity mena = new MenaEntity(
                    menaDetails.getId(),
                    menaDetails.getKod(),
                    menaDetails.getVycetka(),
                    menaDetails.getKodNum(),
                    menaDetails.getZaokrouhleniHotovost(),
                    menaDetails.getZaokrouhleniKarta()
            );
            menaRepository.save(mena);
            return mena.getId();
        } catch (Exception e) {
            throw new CreateException("Failed to create MenaEntity", e);
        }
    }

    public List<MenaEntity> findAll() {
        return menaRepository.findAll();
    }

    public List<MenaEntity> findByCode(String code) {
        return menaRepository.findByCode(code);
    }

    public List<MenaEntity> findByCodeNum(int codeNum) {
        return menaRepository.findByCodeNum(codeNum);
    }


    private List<BigDecimal> parseVycetkaList(String vycetkaDef) {
        List<BigDecimal> vycetka = new ArrayList<>();
        if (vycetkaDef != null && !vycetkaDef.isEmpty()) {
            String[] vycetkaSplit = vycetkaDef.split(VYCETKA_DELIM);
            for (String v : vycetkaSplit) {
                try {
                    vycetka.add(new BigDecimal(v.trim()));
                } catch (NumberFormatException e) {
                    ///  Change to Logger
                    System.err.println("Cannot parse vycetka: " + v);
                }
            }
        }
        return vycetka;
    }

}
