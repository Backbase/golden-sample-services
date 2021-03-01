package com.backbase.goldensample.product.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

import com.backbase.goldensample.product.DockerizedTest;
import com.backbase.goldensample.product.config.IdentityStrategyOverrideConfiguration;
import java.time.LocalDate;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@Slf4j
@Transactional(propagation = NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(IdentityStrategyOverrideConfiguration.class)
class PersistenceIT extends DockerizedTest {

    private static final LocalDate TODAY = LocalDate.of(2020, 1, 28);
    @Autowired
    EntityManagerFactory entityManagerFactory;
    @Autowired
    private ProductRepository repository;
    private ProductEntity savedEntity;

    @BeforeAll
    public static void envSetup() {
        System.setProperty("SIG_SECRET_KEY", "JWTSecretKeyDontUseInProduction!");
    }


    @BeforeEach
    public void setupDb() {

        repository.deleteAll();

        ProductEntity entity = new ProductEntity("amazon", 1, TODAY, Collections.singletonMap("popularity", "87%"));
        savedEntity = repository.save(entity);

        assertEquals(entity, savedEntity);
    }

    @Test
    void create() {
        ProductEntity newEntity = new ProductEntity("n", 2, TODAY, Collections.singletonMap("popularity", "15%"));
        repository.save(newEntity);

        ProductEntity foundEntity = repository.findById(newEntity.getId()).get();

        assertEqualsReview(newEntity, foundEntity);

        assertEquals(2, repository.count());

    }

    @Test
    void update() {

        savedEntity.setName("amazon 2");
        repository.save(savedEntity);

        ProductEntity foundEntity = repository.findById(savedEntity.getId()).get();

        assertEquals("amazon 2", foundEntity.getName());
    }

    @Test
    void delete() {
        repository.delete(savedEntity);
        assertFalse(repository.existsById(savedEntity.getId()));
    }

    @Test
    void getById() {
        ProductEntity foundEntity = repository.findById(savedEntity.getId()).get();

        assertEqualsReview(savedEntity, foundEntity);
    }

    @Test
    void validateEntityEqualityAmongStates() {

        // Generates Entity to validate
      ProductEntity entity = new ProductEntity("amazon", 1, TODAY, Collections.singletonMap("popularity", "87%"));

        // HashSet bucket
        Set<ProductEntity> set = new HashSet<>();

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
            ProductEntity merged = em.merge(entity);
            assertTrue(set.contains(merged));
        });

        // The Entity is considered equal after updating the persistent instance with the identifier of the given detached entity
        runTransaction(em -> {
            em.unwrap(Session.class).update(entity);
            assertTrue(set.contains(entity));
        });

        // The Entity is considered equal after it's loaded in a different persistence context
        runTransaction(em -> {
            ProductEntity found = em.find(ProductEntity.class, entity.getId());
            assertTrue(set.contains(found));
        });

        // The Entity is considered equal after it's loaded as a proxy in a different persistence context
        runTransaction(em -> {
            ProductEntity proxyEntity = em.getReference(ProductEntity.class, entity.getId());
            assertTrue(set.contains(proxyEntity));
            Assertions.assertThat(proxyEntity).isEqualTo(entity);
        });

        // The Entity is considered equal after it's deleted
        runTransaction(em -> {
            ProductEntity toDelete = em.getReference(ProductEntity.class, entity.getId());
            em.remove(toDelete);
            assertTrue(set.contains(toDelete));
        });
    }

    private void assertEqualsReview(ProductEntity expectedEntity, ProductEntity actualEntity) {
        assertEquals(expectedEntity.getId(), actualEntity.getId());
        assertEquals(expectedEntity.getName(), actualEntity.getName());
        assertEquals(expectedEntity.getWeight(), actualEntity.getWeight());
        assertEquals(expectedEntity.getCreateDate(), actualEntity.getCreateDate());
        assertEquals(expectedEntity.getAdditions(), actualEntity.getAdditions());
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
