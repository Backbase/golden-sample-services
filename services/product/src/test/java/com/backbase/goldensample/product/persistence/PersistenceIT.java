package com.backbase.goldensample.product.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

import com.backbase.goldensample.product.DockerizedTest;
import com.backbase.goldensample.product.config.IdentityStrategyOverrideConfiguration;
import java.time.LocalDate;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
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

    /**
     * Validates the equality of this Entity class in different states and with different data. Due to the definition of
     * our Entity, 2 suppress warnings are required in order to validate it as explained in <a
     * href="https://jqno.nl/equalsverifier/manual/jpa-entities/">here</a>.
     */
    @Test
    void validateEntityEqualityAmongStates() {
        EqualsVerifier.forClass(ProductEntity.class)
            .suppress(Warning.IDENTICAL_COPY_FOR_VERSIONED_ENTITY)
            .suppress(Warning.STRICT_HASHCODE)
            .verify();
    }

    private void assertEqualsReview(ProductEntity expectedEntity, ProductEntity actualEntity) {
        assertEquals(expectedEntity.getId(), actualEntity.getId());
        assertEquals(expectedEntity.getName(), actualEntity.getName());
        assertEquals(expectedEntity.getWeight(), actualEntity.getWeight());
        assertEquals(expectedEntity.getCreateDate(), actualEntity.getCreateDate());
        assertEquals(expectedEntity.getAdditions(), actualEntity.getAdditions());
    }

}
