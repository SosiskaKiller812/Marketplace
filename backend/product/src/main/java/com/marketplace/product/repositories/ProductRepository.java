package com.marketplace.product.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.marketplace.product.entities.Category;
import com.marketplace.product.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByName(String name);

    List<Product> findByCategoriesContainingOrderByNameAsc(Category category);

    List<Product> findByNameContaining(String name);

    boolean existsByName(String name);

    @Modifying
    @Query(value = "DELETE FROM product_category WHERE category_id = :categoryId", 
           nativeQuery = true)
    void deleteCategoryRelations(@Param("categoryId") int categoryId);

}
