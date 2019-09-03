package com.cmdelivery.repository;

import com.cmdelivery.model.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository()
public interface PartnerRepository extends JpaRepository<Partner, Long> {
    void deleteAll();
    Partner findByName(String name);
}
