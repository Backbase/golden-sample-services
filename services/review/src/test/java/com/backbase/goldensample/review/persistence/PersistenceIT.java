package com.backbase.goldensample.review.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;


import com.backbase.goldensample.review.config.IdentityStrategyOverrideConfiguration;
import java.util.Collections;
import org.junit.Assert;
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
@Transactional(propagation = NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(IdentityStrategyOverrideConfiguration.class)
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

    ReviewEntity entity = new ReviewEntity(1L, "author", "subject", "content", Collections.singletonMap("verified","true"));
    savedEntity = repository.save(entity);

    assertEquals(entity, savedEntity);
  }

  @Test
  void create() {
    ReviewEntity newEntity = new ReviewEntity(1L, "author", "subject", "content", Collections.singletonMap("verified","true"));
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
    assertEquals(Collections.EMPTY_MAP, foundEntity.getAdditions());
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
    Assert.assertEquals(expectedEntity.getId(),        actualEntity.getId());
    Assert.assertEquals(expectedEntity.getProductId(),        actualEntity.getProductId());
    Assert.assertEquals(expectedEntity.getAuthor(),    actualEntity.getAuthor());
    Assert.assertEquals(expectedEntity.getContent(),   actualEntity.getContent());
    Assert.assertEquals(expectedEntity.getSubject(),   actualEntity.getSubject());
  }
}
