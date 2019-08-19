package com.cmdelivery.repository;

import com.cmdelivery.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository()
public interface PersonRepository extends JpaRepository<Person, Long> {
    void deleteAll();
    Person findByPhone(String phone);
}
