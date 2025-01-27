package RepositoryTests.Common;


import RepositoryTests.TestDataSourceConfig;
import cz.inspire.common.entity.HeaderEntity;
import cz.inspire.common.repository.HeaderRepository;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.TransactionScoped;
import jakarta.transaction.UserTransaction;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

@ExtendWith(ArquillianExtension.class)
public class HeaderRepositoryTest {

    // If your repository is discovered by the Jakarta Data extension,
    // it will be injected here automatically
    @Inject
    private HeaderRepository headerRepository;

    // If you need direct access for manual transactions,
    // you can also inject an EntityManager or a UserTransaction
    @Inject
    private EntityManager em;

    @Inject
    private UserTransaction utx;

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                // Add classes: your entities, repositories, data-source config
                .addClasses(
                        HeaderEntity.class,
                        HeaderRepository.class,
                        // e.g., your @Singleton class with @DataSourceDefinition
                        TestDataSourceConfig.class
                )
                .addAsLibraries(
                        Maven.resolver()
                                .resolve("org.postgresql:postgresql:42.7.4")
                                .withTransitivity()
                                .asFile()
                )
                // Add your persistence.xml
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @TransactionScoped
    @Test
    public void testFindValidAttributes() throws Exception {
        // Because we're in a container environment, let's manually handle transaction boundaries:
        utx.begin();
        em.joinTransaction();

        // Insert test data
        HeaderEntity e1 = new HeaderEntity("id1", 10, 0);
        HeaderEntity e2 = new HeaderEntity("id2", 20, 5);
        HeaderEntity e3 = new HeaderEntity("id3", 30, -1);

        em.getTransaction().begin();
        em.persist(e1);
        em.persist(e2);
        em.persist(e3);
        em.getTransaction().commit();
        utx.commit();

        utx.begin();
        em.joinTransaction();
        List<HeaderEntity> entities = em.createQuery("SELECT h FROM HeaderEntity h", HeaderEntity.class).getResultList();
        utx.commit();

        System.out.println("Persisted entities: " + entities);
        Assertions.assertEquals(3, entities.size());


        // Now call the repository method
        var valid = headerRepository.findValidAttributes();
        // Expect e1 and e2, but not e3
        Assertions.assertEquals(1, valid.size(), "Should return 2 rows");
    }
}