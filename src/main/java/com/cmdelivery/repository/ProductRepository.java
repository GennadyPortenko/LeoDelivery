package com.cmdelivery.repository;

import com.cmdelivery.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    void deleteAll();
    Product findByName(String name);
    Product findByProductId(int id);
}
