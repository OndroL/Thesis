package cz.inspire.thesis.data.service;

import cz.inspire.thesis.data.model.Header;
import cz.inspire.thesis.data.repository.HeaderRepository;
import jakarta.inject.Inject;

import java.util.List;

public class HeaderService {
    @Inject
    private HeaderRepository headerRepository;

    public Header save(int field, int location) {
        Header header = new Header();
        header.setField(field);
        header.setLocation(location);

        return headerRepository.save(header);
    }

    public List<Header> findValidAtributes()
    {
        return headerRepository.findValidAtributes();
    }
}
