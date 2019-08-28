package com.cmdelivery.repository;

import com.cmdelivery.model.District;
import com.cmdelivery.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistrictRepository extends JpaRepository<District, Long> {
}
