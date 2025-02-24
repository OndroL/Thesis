//package cz.inspire.utils.repository;
//
//import jakarta.enterprise.context.ApplicationScoped;
//import jakarta.enterprise.inject.Produces;
//import jakarta.enterprise.inject.spi.InjectionPoint;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//
//@ApplicationScoped
//public class InstructorTestRepositoryProducer {
//
//    @PersistenceContext
//    EntityManager em;
//
//    @Produces
//    public InstructorTestRepository produceRepository(InjectionPoint ip) {
//        return RepositoryFactory.createRepository(InstructorTestRepository.class, em);
//    }
//}
