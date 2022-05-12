package com.backbase.goldensample.product.persistence;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.PagedList;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {
 
    @PersistenceContext
    private EntityManager entityManager;
 
    @Autowired
    private CriteriaBuilderFactory criteriaBuilderFactory;

    public ProductRepositoryCustomImpl(CriteriaBuilderFactory criteriaBuilderFactory) {
        this.criteriaBuilderFactory = criteriaBuilderFactory;
    }

    @Override
    public PagedList<ProductEntity> findTopN(Sort sortBy, int pageSize) {
        return sortedCriteriaBuilder(sortBy)
            .page(0, pageSize)
            .withKeysetExtraction(true)
            .getResultList();
    }
 
    @Override
    public PagedList<ProductEntity> findNextN(Sort sortBy, PagedList<ProductEntity> previousPage) {
        return sortedCriteriaBuilder(sortBy).page(previousPage.getKeysetPage(),
                previousPage.getPage() * previousPage.getMaxResults(),
                   previousPage.getMaxResults()
            )
            .getResultList();
    }

    private CriteriaBuilder<ProductEntity> sortedCriteriaBuilder(Sort sortBy) {
        CriteriaBuilder<ProductEntity> criteriaBuilder = criteriaBuilderFactory.create(entityManager, ProductEntity.class);
        sortBy.forEach(order -> {criteriaBuilder.orderBy(order.getProperty(), order.isAscending());});

        return criteriaBuilder;
    }
}
