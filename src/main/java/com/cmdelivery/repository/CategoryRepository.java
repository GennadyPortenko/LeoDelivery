package com.cmdelivery.repository;

import com.cmdelivery.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByCategoryId(int id);
    List<Category> findByNameEnIn(List<String> names);
    List<Category> findByNameFrIn(List<String> names);
}
