package com.cmdelivery.repository;

import com.cmdelivery.model.City;
import com.cmdelivery.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
}
