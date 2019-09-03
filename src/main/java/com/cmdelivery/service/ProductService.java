package com.cmdelivery.service;

import com.cmdelivery.dto.ProductDto;
import com.cmdelivery.model.Product;
import com.cmdelivery.model.Section;
import com.cmdelivery.repository.PartnerRepository;
import com.cmdelivery.repository.ProductRepository;
import com.cmdelivery.repository.SectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class ProductService {
    private final ProductRepository productRepository;
    private final SectionRepository sectionRepository;
    private final PartnerRepository partnerRepository;
    private final SecurityService securityService;

    public Product registerNewProduct(Product product) {
        Section defaultSection =
            sectionRepository.findByNameAndPartner(SectionService.defaultSectionName(),
                                                      partnerRepository.findByName(securityService.getCurrentUserName()).getPartnerId());
        product.setSection(defaultSection);
        return productRepository.save(product);
    }

    public void modifyProduct (Product product, ProductDto newProductDto) {
        product.setName(newProductDto.getName());
        product.setDescription(newProductDto.getDescription());
    }

}
