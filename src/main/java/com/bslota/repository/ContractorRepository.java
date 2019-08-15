package com.bslota.repository;

import com.bslota.model.Contractor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository()
public interface ContractorRepository extends JpaRepository<Contractor, Long> {
    void deleteAll();
    Contractor findByName(String name);
}
