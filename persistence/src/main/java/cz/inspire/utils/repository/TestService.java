package cz.inspire.utils.repository;

import jakarta.inject.Inject;

public class TestService {
    @Inject
    InstructorTestRepository instructorTestRepository;

    public void b() {
        instructorTestRepository.findAllByDeleted(true, 1,1);
    }

}
