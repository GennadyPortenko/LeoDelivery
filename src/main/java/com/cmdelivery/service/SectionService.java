package com.cmdelivery.service;

import com.cmdelivery.model.Section;
import com.cmdelivery.repository.SectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class SectionService {
    private final SectionRepository sectionRepository;

    public static String defaultSectionName() { return "Other"; }

    public Section registerNewSection(Section section) {
        if (section.getName().equals(SectionService.defaultSectionName())) {
            return null;
        }
        return sectionRepository.save(section);
    }

    public Integer deleteById(int id) {
        if (!sectionRepository.findBySectionId(id).getName().equals(defaultSectionName())) {
            return sectionRepository.deleteBySectionId(id);
        }
        return null;
    }
}
