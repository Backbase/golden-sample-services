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
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import lombok.extern.slf4j.Slf4j;
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
    EntityManagerFactory entityManagerFactory;
    @Autowired
    private ReviewRepository repository;
    private ReviewEntity savedEntity;

    @BeforeAll
    public static void envSetup() {
        System.setProperty("SIG_SECRET_KEY", "JWTSecretKeyDontUseInProduction!");
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

    @Test
    void validateEntityEqualityAmongStates() {

        // Generates Entity to validate
        ReviewEntity entity = new ReviewEntity(1L, "author", "subject", "content", 5,
            Collections.singletonMap("verified", "true"));

        // HashSet bucket
        Set<ReviewEntity> set = new HashSet<>();

        assertFalse(set.contains(entity));
        set.add(entity);
        assertTrue(set.contains(entity));

        // The Entity is considered equal before persisting/flushing (i.e. generating the primary key) and afterwards
        runTransaction(em -> {
            em.persist(entity);
            em.flush();
            assertTrue(set.contains(entity));
        });

        // The Entity is considered equal after reattaching it to a new Persistence Context
        runTransaction(em -> {
            ReviewEntity merged = em.merge(entity);
            assertTrue(set.contains(merged));
        });

        // The Entity is considered equal after updating the persistent instance with the identifier of the given detached entity
        runTransaction(em -> {
            em.unwrap(Session.class).update(entity);
            assertTrue(set.contains(entity));
        });

        // The Entity is considered equal after it's loaded in a different persistence context
        runTransaction(em -> {
            ReviewEntity found = em.find(ReviewEntity.class, entity.getId());
            assertTrue(set.contains(found));
        });

        // The Entity is considered equal after it's loaded as a proxy in a different persistence context
        runTransaction(em -> {
            ReviewEntity proxyEntity = em.getReference(ReviewEntity.class, entity.getId());
            assertTrue(set.contains(proxyEntity));
            Assertions.assertThat(proxyEntity).isEqualTo(entity);
        });

        // The Entity is considered equal after it's deleted
        runTransaction(em -> {
            ReviewEntity toDelete = em.getReference(ReviewEntity.class, entity.getId());
            em.remove(toDelete);
            assertTrue(set.contains(toDelete));
        });
    }

    private void assertEqualsReview(ReviewEntity expectedEntity, ReviewEntity actualEntity) {
        assertEquals(expectedEntity.getId(), actualEntity.getId());
        assertEquals(expectedEntity.getProductId(), actualEntity.getProductId());
        assertEquals(expectedEntity.getAuthor(), actualEntity.getAuthor());
        assertEquals(expectedEntity.getContent(), actualEntity.getContent());
        assertEquals(expectedEntity.getSubject(), actualEntity.getSubject());
    }

    private void runTransaction(Consumer<EntityManager> consumer) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction txn = null;
        try {
            txn = entityManager.getTransaction();
            txn.begin();
            consumer.accept(entityManager);
            if (!txn.getRollbackOnly()) {
                txn.commit();
            } else {
                try {
                    txn.rollback();
                } catch (Exception e) {
                    log.error("Rollback failure", e);
                }
            }
        } catch (Throwable t) {
            if (txn != null && txn.isActive()) {
                try {
                    txn.rollback();
                } catch (Exception e) {
                    log.error("Rollback failure", e);
                }
            }
            throw t;
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

}
