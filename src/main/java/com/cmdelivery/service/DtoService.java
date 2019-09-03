package com.cmdelivery.service;

import com.cmdelivery.dto.ContractorDto;
import com.cmdelivery.dto.ContractorSettingsDto;
import com.cmdelivery.dto.ProductDto;
import com.cmdelivery.dto.SectionDto;
import com.cmdelivery.model.Contractor;
import com.cmdelivery.model.Product;
import com.cmdelivery.model.Section;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class DtoService {
    private final ModelMapper modelMapper;

    @Value("${contractor.image.url}")
    private String contractorImageUrl;
    @Value("${product.image.url}")
    private String productImageUrl;

    public static String parsePhone(String phone) {
        return phone.replaceAll("[^\\d.]", "").substring(1);
    }

    public static String toMaskedPhone(String phone) {
        return "+7 (" + phone.substring(0, 3) + ") " + phone.substring(3, 6) + "-" + phone.substring(6, 10);
    }

    public ProductDto convertToDto(Product product) {
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        productDto.setId(product.getProductId());
        String productImage = product.getImage();
        productDto.setImage(productImage == null ? null :
                                ServletUriComponentsBuilder.fromCurrentContextPath()
                                    .path(productImageUrl)
                                    .path(productImage)
                                    .toUriString());
        return productDto;
    }

    public Product convertToProduct(ProductDto productDto) {
        Product product = modelMapper.map(productDto, Product.class);
        return product;
    }

    public SectionDto convertToDto(Section section) {
        SectionDto sectionDto = modelMapper.map(section, SectionDto.class);
        sectionDto.setId(section.getSectionId());
        sectionDto.setProducts(section.getProducts().stream().map(this::convertToDto).collect(Collectors.toList()));
        return sectionDto;
    }

    public Section convertToSection(SectionDto sectionDto) {
        Section section = modelMapper.map(sectionDto, Section.class);
        return section;
    }

    public ContractorDto convertToDto(Contractor contractor) {
        ContractorDto contractorDto = modelMapper.map(contractor, ContractorDto.class);
        contractorDto.setId(contractor.getContractorId());
        contractorDto.setSections(contractor.getSections().stream().map(this::convertToDto).collect(Collectors.toList()));
        String contractorImage = contractor.getImage();
        contractorDto.setImage(contractorImage == null ? null :
                                ServletUriComponentsBuilder.fromCurrentContextPath()
                                    .path(contractorImageUrl)
                                    .path(contractorImage)
                                    .toUriString());
        return contractorDto;
    }

    public Contractor convertToContractor(ContractorDto contractorDto) {
        Contractor contractor = modelMapper.map(contractorDto, Contractor.class);
        return contractor;
    }

    public ContractorSettingsDto getContractorSettings(Contractor contractor) {
        return new ContractorSettingsDto(contractor.getMinTime(), contractor.getMaxTime(), contractor.getMinPrice());
    }

}
