package com.cmdelivery.repository;

import com.cmdelivery.model.Category;
import com.cmdelivery.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Role findByName(String name);
}
