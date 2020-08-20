package com.backbase.goldensample.product;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;


import com.backbase.goldensample.product.persistence.ProductEntity;
import com.backbase.goldensample.product.persistence.ProductRepository;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional(propagation = NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PersistenceIT {

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

    repository.deleteAll();

    ProductEntity entity = new ProductEntity("amazon", 1, TODAY);
    savedEntity = repository.save(entity);

    assertEquals(entity, savedEntity);
  }

  @Test
  public void create() {
    ProductEntity newEntity = new ProductEntity("n", 2, TODAY);
    repository.save(newEntity);

    ProductEntity foundEntity = repository.findById(newEntity.getId()).get();

    assertEqualsReview(newEntity, foundEntity);

    assertEquals(2, repository.count());

  }

  @Test
  public void update() {

    savedEntity.setName("amazon 2");
    repository.save(savedEntity);

    ProductEntity foundEntity = repository.findById(savedEntity.getId()).get();

    assertEquals(1, (long) foundEntity.getVersion());
    assertEquals("amazon 2", foundEntity.getName());
  }

  @Test
  public void delete() {
    repository.delete(savedEntity);
    assertFalse(repository.existsById(savedEntity.getId()));
  }

  @Test
  public void getById() {
    ProductEntity foundEntity = repository.findById(savedEntity.getId()).get();

    assertEqualsReview(savedEntity, foundEntity);
  }

//  @Test
//  public void duplicateError() {
//
//    Assertions.assertThrows(
//        DataIntegrityViolationException.class,
//        () -> repository.save(new ProductEntity("amazon 1", 20, TODAY)));
//  }

  @Test
  public void optimisticLockError() {

    // Store the saved entity in two separate entity objects
    ProductEntity entity1 = repository.findById(savedEntity.getId()).orElse(new ProductEntity()),
        entity2 = repository.findById(savedEntity.getId()).orElse(new ProductEntity());

    // Update the entity using the first entity object
    entity1.setName("amazon 1");
    repository.save(entity1);

    /*
      Update the entity using the second entity object.
      This should fail since the second entity now holds a old version number,
      i.e. a Optimistic Lock Error
    */
    try {
      entity2.setName("amazon 2");
      repository.save(entity2);

      fail("Expected an OptimisticLockingFailureException");
    } catch (OptimisticLockingFailureException ignored) {
    }

    // Get the updated entity from the database and verify its new sate
    var updatedEntity = repository.findById(savedEntity.getId()).orElse(new ProductEntity());

    assertEquals(1, updatedEntity.getVersion());
    assertEquals("amazon 1", updatedEntity.getName());
  }

  private void assertEqualsReview(ProductEntity expectedEntity, ProductEntity actualEntity) {
    Assert.assertEquals(expectedEntity.getId(),        actualEntity.getId());
    Assert.assertEquals(expectedEntity.getVersion(),   actualEntity.getVersion());
    Assert.assertEquals(expectedEntity.getName(),    actualEntity.getName());
    Assert.assertEquals(expectedEntity.getWeight(),   actualEntity.getWeight());
    Assert.assertEquals(expectedEntity.getCreateDate(),   actualEntity.getCreateDate());
  }
}
