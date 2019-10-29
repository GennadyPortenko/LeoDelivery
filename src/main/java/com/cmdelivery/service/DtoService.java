package com.cmdelivery.service;

import com.cmdelivery.dto.*;
import com.cmdelivery.model.Category;
import com.cmdelivery.model.Partner;
import com.cmdelivery.model.Product;
import com.cmdelivery.model.Section;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class DtoService {
    private final ModelMapper modelMapper;

    @Value("${partner.image.url}")
    private String partnerImageUrl;
    @Value("${partner.image.url}")
    private String partnerLogoUrl;
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

    public PartnerDto convertToDto(Partner partner) {
        PartnerDto partnerDto = modelMapper.map(partner, PartnerDto.class);
        partnerDto.setId(partner.getPartnerId());
        partnerDto.setSections(partner.getSections().stream().map(this::convertToDto).collect(Collectors.toList()));
        partnerDto.setCategories(partner.getCategories().stream().map(this::convertToDto).collect(Collectors.toList()));
        partnerDto.setMainCategory(convertToDto(partner.getMainCategory()));
        String partnerImage = partner.getImage();
        partnerDto.setImage(partnerImage == null ? null :
                                ServletUriComponentsBuilder.fromCurrentContextPath()
                                    .path(partnerImageUrl)
                                    .path(partnerImage)
                                    .toUriString());
        String partnerLogo = partner.getLogo();
        partnerDto.setLogo(partnerLogo == null ? null :
                ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path(partnerLogoUrl)
                        .path(partnerLogo)
                        .toUriString());
        return partnerDto;
    }

    public Partner convertToPartner(PartnerDto partnerDto) {
        Partner partner = modelMapper.map(partnerDto, Partner.class);
        return partner;
    }

    public PartnerSettingsDto getPartnerSettings(Partner partner) {
        return new PartnerSettingsDto(partner.getMinTime(), partner.getMaxTime(), partner.getMinPrice());
    }

    public Category convertToCategory(CategoryDto categoryDto) {
        return modelMapper.map(categoryDto, Category.class);
    }

    public CategoryDto convertToDto(Category category) {
        CategoryDto categoryDto = modelMapper.map(category, CategoryDto.class);
        String localeString = LocaleContextHolder.getLocale().toString();
        if (localeString.equals("fr_FR")) {
            categoryDto.setName(category.getNameFr());
        } else {
            categoryDto.setName(category.getNameEn());
        }
        categoryDto.setId(category.getCategoryId());
        return categoryDto;
    }

}
