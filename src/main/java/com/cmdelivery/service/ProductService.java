package com.cmdelivery.service;

import com.cmdelivery.model.Product;
import com.cmdelivery.model.Section;
import com.cmdelivery.repository.ContractorRepository;
import com.cmdelivery.repository.ProductRepository;
import com.cmdelivery.repository.SectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Security;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class ProductService {
    private final ProductRepository productRepository;
    private final SectionRepository sectionRepository;
    private final ContractorRepository contractorRepository;
    private final SecurityService securityService;

    public Product registerNewProduct(Product product) {
        Section defaultSection =
            sectionRepository.findByNameAndContractor(SectionService.defaultSectionName(),
                                                      contractorRepository.findByName(securityService.getCurrentUserName()).getContractorId());
        product.setSection(defaultSection);
        return productRepository.save(product);
    }

}
