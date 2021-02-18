package com.backbase.goldensample.product.service;


import com.backbase.product.api.service.v1.model.Product;
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
   * @param id is the product id that you are looking for. * @param delay Causes the getProduct API
   *     on the product microservice to delay its response. * The parameter is specified in seconds
   * @param faultPercent Causes the getProduct API on the product microservice to throw an exception
   *     randomly with the probability specified by the query parameter, * from 0 to 100%. For
   *     example, if the parameter is set to 25, it will cause * every fourth call to the API, on
   *     average, to fail with an exception.
   * @return the product, if found, else null.
   * @since v0.1
   */
  Product getProduct(long id, int delay, int faultPercent);

  /**
   * Get all the products
   *
   *
   * @since v0.1
   */
  List<Product> getAllProducts();

  /**
   * Add product to the repository.
   *
   * @param body product to save.
   * @since v0.1
   */
  default Product createProduct(Product body) {
    return null;
  }

  /**
   * Update product to the repository.
   *
   * @param body product to save.
   * @since v0.1
   */
  default Product updateProduct(Product body) {
    return null;
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
