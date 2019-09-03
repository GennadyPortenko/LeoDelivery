package com.cmdelivery.service;

import com.cmdelivery.dto.SectionDto;
import com.cmdelivery.model.Partner;
import com.cmdelivery.model.Product;
import com.cmdelivery.model.Section;
import com.cmdelivery.repository.ProductRepository;
import com.cmdelivery.repository.SectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class SectionService {
    private final SectionRepository sectionRepository;
    private final SecurityService securityService;
    private final ProductRepository productRepository;

    public static String defaultSectionName() { return "Other"; }

    public Section registerNewSection(Section section) {
        if (section.getName().equals(SectionService.defaultSectionName())) {
            return null;
        }
        return sectionRepository.save(section);
    }

    public Section registerNewDefaultSection(Section section) {
        section.setName(defaultSectionName());
        return sectionRepository.save(section);
    }

    public Integer deleteById(long id) {
        if (!sectionRepository.findBySectionId(id).getName().equals(defaultSectionName())) {
            return sectionRepository.deleteBySectionId(id);
        }
        return null;
    }

    public boolean isDefault(Section section) {
        return section.getName().equalsIgnoreCase(defaultSectionName());
    }

    public void modifySection(Section section, SectionDto newSectionDto) {
        section.setName(newSectionDto.getName());
        section.setDescription(newSectionDto.getDescription());
    }

    @Transactional
    public boolean removeSection(Section section, Section defaultSection) {
        Set<Product> sectionProducts = section.getProducts();
        for (Product product : sectionProducts) {
            product.setSection(defaultSection);
            if (productRepository.save(product) == null) {
               return false;
            }
        }
        sectionRepository.delete(section);
        return true;
    }
}
