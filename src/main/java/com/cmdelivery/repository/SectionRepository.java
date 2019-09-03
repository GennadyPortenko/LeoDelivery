package com.cmdelivery.repository;

import com.cmdelivery.model.Partner;
import com.cmdelivery.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {
    void deleteAll();
    Section findByName(String name);
    Section findBySectionId(long id);
    Integer deleteBySectionId(long id);

    @Query(nativeQuery = true, value = "SELECT * FROM section WHERE name = ?1 AND partner_fk = ?2")
    Section findByNameAndPartner(String name, Long partnerId);
}
