package com.marketplace.product.repositories;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.marketplace.product.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
  Optional<Category> findByName(String name);

  @Query("SELECT c FROM Category c WHERE UPPER(c.name) IN:categories")
  List<Category> findAllByName(@Param("categories") Collection<String> categories);

  boolean existsByName(String name);

}
