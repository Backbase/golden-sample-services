package com.backbase.goldensample.review;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;


import com.backbase.goldensample.review.persistence.ReviewEntity;
import com.backbase.goldensample.review.persistence.ReviewRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional(propagation = NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PersistenceIT {

  @Autowired private ReviewRepository repository;

  private ReviewEntity savedEntity;

  @BeforeAll
  public static void envSetup() {
    System.setProperty("SIG_SECRET_KEY", "JWTSecretKeyDontUseInProduction!");
  }


  @BeforeEach
  public void setupDb() {

    repository.deleteAll();

    ReviewEntity entity = new ReviewEntity(1L, "author", "subject", "content");
    savedEntity = repository.save(entity);

    assertEquals(entity, savedEntity);
  }

  @Test
  public void create() {
    ReviewEntity newEntity = new ReviewEntity(1L, "author", "subject", "content");
    repository.save(newEntity);

    ReviewEntity foundEntity = repository.findById(newEntity.getId()).get();

    assertEqualsReview(newEntity, foundEntity);

    assertEquals(2, repository.count());

  }

  @Test
  public void update() {

    savedEntity.setAuthor("amazon 2");
    repository.save(savedEntity);

    ReviewEntity foundEntity = repository.findById(savedEntity.getId()).get();

    assertEquals("amazon 2", foundEntity.getAuthor());
  }

  @Test
  public void delete() {
    repository.delete(savedEntity);
    assertFalse(repository.existsById(savedEntity.getId()));
  }

  @Test
  public void getById() {
    ReviewEntity foundEntity = repository.findById(savedEntity.getId()).get();

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
//  public void optimisticLockError() {
//
//    // Store the saved entity in two separate entity objects
//    ReviewEntity entity1 = repository.findById(savedEntity.getId()).orElse(new ReviewEntity()),
//        entity2 = repository.findById(savedEntity.getId()).orElse(new ReviewEntity());
//
//    // Update the entity using the first entity object
//    entity1.setAuthor("amazon 1");
//    repository.save(entity1);
//
//    /*
//      Update the entity using the second entity object.
//      This should fail since the second entity now holds a old version number,
//      i.e. a Optimistic Lock Error
//    */
//    try {
//      entity2.setAuthor("amazon 2");
//      repository.save(entity2);
//
//      fail("Expected an OptimisticLockingFailureException");
//    } catch (OptimisticLockingFailureException ignored) {
//    }
//
//    // Get the updated entity from the database and verify its new sate
//    var updatedEntity = repository.findById(savedEntity.getId()).orElse(new ReviewEntity());
//
//    assertEquals(1, updatedEntity.getVersion());
//    assertEquals("amazon 1", updatedEntity.getAuthor());
//  }

  private void assertEqualsReview(ReviewEntity expectedEntity, ReviewEntity actualEntity) {
    Assert.assertEquals(expectedEntity.getId(),        actualEntity.getId());
    Assert.assertEquals(expectedEntity.getProductId(),        actualEntity.getProductId());
    Assert.assertEquals(expectedEntity.getAuthor(),    actualEntity.getAuthor());
    Assert.assertEquals(expectedEntity.getContent(),   actualEntity.getContent());
    Assert.assertEquals(expectedEntity.getSubject(),   actualEntity.getSubject());
  }
}
