package com.cmdelivery.repository;

import com.cmdelivery.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository()
public interface SectionRepository extends JpaRepository<Section, Long> {
    void deleteAll();
    Section findByName(String name);
    Section findBySectionId(int id);
    Integer deleteBySectionId(int id);
}
