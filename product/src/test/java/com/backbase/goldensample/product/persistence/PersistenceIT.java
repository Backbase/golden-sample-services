package com.backbase.goldensample.product.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;


import com.backbase.goldensample.product.DockerizedTest;
import com.backbase.goldensample.product.config.IdentityStrategyOverrideConfiguration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.junit.Assert;
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
@Transactional(propagation = NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(IdentityStrategyOverrideConfiguration.class)
class PersistenceIT extends DockerizedTest {

  @Autowired private ProductRepository repository;

  private ProductEntity savedEntity;

  private static final Instant TODAY = LocalDateTime.of(2020, 1, 28, 12, 26)
      .toInstant(ZoneOffset.UTC);

  @BeforeAll
  public static void envSetup() {
    System.setProperty("SIG_SECRET_KEY", "JWTSecretKeyDontUseInProduction!");
  }


  @BeforeEach
  public void setupDb() {

//    repository.deleteAll();

    ProductEntity entity = new ProductEntity("amazon", 1, TODAY);
    savedEntity = repository.save(entity);

    assertEquals(entity, savedEntity);
  }

  @Test
  void create() {
    ProductEntity newEntity = new ProductEntity("n", 2, TODAY);
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

  private void assertEqualsReview(ProductEntity expectedEntity, ProductEntity actualEntity) {
    Assert.assertEquals(expectedEntity.getId(),        actualEntity.getId());
    Assert.assertEquals(expectedEntity.getName(),    actualEntity.getName());
    Assert.assertEquals(expectedEntity.getWeight(),   actualEntity.getWeight());
    Assert.assertEquals(expectedEntity.getCreateDate(),   actualEntity.getCreateDate());
  }
}
