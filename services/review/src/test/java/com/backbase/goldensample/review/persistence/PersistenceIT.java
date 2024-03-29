package com.backbase.goldensample.review.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

import com.backbase.goldensample.review.config.IdentityStrategyOverrideConfiguration;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import lombok.extern.slf4j.Slf4j;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.assertj.core.api.Assertions;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringRunner.class)
@DataJpaTest
@Slf4j
@Transactional(propagation = NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(IdentityStrategyOverrideConfiguration.class)
class PersistenceIT {

    @Autowired
    private ReviewRepository repository;
    private ReviewEntity savedEntity;

    @BeforeAll
    public static void envSetup() {
        System.setProperty("SIG_SECRET_KEY", "JWTSecretKeyDontUseInProduction!");
        //Liquibase should work offline with your service, as no production environment will allow outgoing connectivity for security reasons
        System.setProperty("socksProxyHost", "127.0.0.1");
    }


    @BeforeEach
    public void setupDb() {

        repository.deleteAll();

        ReviewEntity entity = new ReviewEntity(1L, "author", "subject", "content", 5,
            Collections.singletonMap("verified", "true"));
        savedEntity = repository.save(entity);

        assertEquals(entity, savedEntity);
    }

    @Test
    void create() {
        ReviewEntity newEntity = new ReviewEntity(1L, "author", "subject", "content", 5,
            Collections.singletonMap("verified", "true"));
        repository.save(newEntity);

        ReviewEntity foundEntity = repository.findById(newEntity.getId()).get();

        assertEqualsReview(newEntity, foundEntity);

        assertEquals(2, repository.count());

    }

    @Test
    void update() {

        savedEntity.setAuthor("amazon 2");
        savedEntity.setAdditions(null);
        repository.save(savedEntity);

        ReviewEntity foundEntity = repository.findById(savedEntity.getId()).get();

        assertEquals("amazon 2", foundEntity.getAuthor());
        assertNull(foundEntity.getAdditions());
    }

    @Test
    void delete() {
        repository.delete(savedEntity);
        assertFalse(repository.existsById(savedEntity.getId()));
    }

    @Test
    void getById() {
        ReviewEntity foundEntity = repository.findById(savedEntity.getId()).get();

        assertEqualsReview(savedEntity, foundEntity);
    }

    private void assertEqualsReview(ReviewEntity expectedEntity, ReviewEntity actualEntity) {
        assertEquals(expectedEntity.getId(), actualEntity.getId());
        assertEquals(expectedEntity.getProductId(), actualEntity.getProductId());
        assertEquals(expectedEntity.getAuthor(), actualEntity.getAuthor());
        assertEquals(expectedEntity.getContent(), actualEntity.getContent());
        assertEquals(expectedEntity.getSubject(), actualEntity.getSubject());
    }

}
