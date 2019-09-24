package com.cmdelivery.repository;

import com.cmdelivery.model.Category;
import com.cmdelivery.model.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository()
public interface PartnerRepository extends JpaRepository<Partner, Long> {
    void deleteAll();
    Partner findByName(String name);
    Set<Partner> findByMainCategoryIn(List<Category> categories);
}
