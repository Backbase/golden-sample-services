package com.backbase.goldensample.review.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

  @Transactional(readOnly = true)
  List<ReviewEntity> findByProductId(long productId);
}
