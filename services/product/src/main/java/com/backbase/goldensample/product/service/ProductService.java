package com.backbase.goldensample.product.service;


import com.backbase.goldensample.product.persistence.ProductEntity;
import com.backbase.product.api.service.v1.model.Product;
import com.backbase.product.api.service.v1.model.ProductId;
import java.util.List;

/**
 * Interface that define the general service contract (methods) for the Product
 *
 * <ol>
 *   <li>Service and,
 *   <li>Controller interfaces.
 * </ol>
 *
 */
public interface ProductService {

  /**
   * Get the product with Id from repository. It is a Non-Blocking API.
   *
   * @param id is the product id that you are looking for.
   * @return the product, if found, else null.
   * @since v0.1
   */
  ProductEntity getProduct(long id);

  /**
   * Get all the products
   *
   *
   * @since v0.1
   * @return
   */
  List<ProductEntity> getAllProducts();

  /**
   * Add product to the repository.
   *
   * @param body product to save.
   * @since v0.1
   */
  default ProductId createProduct(Product body) {
    return null;
  }

  /**
   * Update product to the repository.
   *
   * @param body product to save.
   * @since v0.1
   */
  default void updateProduct(Product body) {
  }

  /**
   * Delete the product from repository.
   *
   * @implNote This method should be idempotent and always return 200 OK status.
   * @param id to be deleted.
   * @since v0.1
   */
  default void deleteProduct(long id){}
}
