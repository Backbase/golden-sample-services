package com.backbase.goldensample.product.persistence;

import com.blazebit.persistence.PagedList;
import org.springframework.data.domain.Sort;

public interface ProductRepositoryCustom {

    PagedList<ProductEntity> findTopN(
        Sort sortBy,
        int pageSize
    );

    PagedList<ProductEntity> findNextN(
        Sort orderBy,
        PagedList<ProductEntity> previousPage
    );
}
