package com.cmdelivery.controller;

import com.cmdelivery.dto.FileUploadResponse;
import com.cmdelivery.dto.ProductDto;
import com.cmdelivery.dto.SectionDto;
import com.cmdelivery.exception.Error403Exception;
import com.cmdelivery.exception.Error404Exception;
import com.cmdelivery.model.Contractor;
import com.cmdelivery.model.Product;
import com.cmdelivery.model.Section;
import com.cmdelivery.repository.ContractorRepository;
import com.cmdelivery.repository.ProductRepository;
import com.cmdelivery.repository.SectionRepository;
import com.cmdelivery.service.*;
import com.cmdelivery.validation.ValidImage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class CabinetController {
    private final ContractorRepository contractorRepository;
    private final ContractorService contractorService;
    private final SecurityService securityService;
    private final DtoService dtoService;
    private final SectionService sectionService;
    private final SectionRepository sectionRepository;
    private final ProductService productService;
    private final ProductRepository productRepository;
    private final IStorageService storageService;

    @Value("${contractor.image.url}")
    private String contractorImageUrl;
    @Value("${product.image.url}")
    private String productImageUrl;

    @GetMapping(value="/cabinet")
    public ModelAndView contractorHome(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("contractor/cabinet");
        String username = securityService.getCurrentUserName();
        Contractor contractor = contractorRepository.findByName(username);
        if (contractor == null) {
            return modelAndView;
        }
        List<SectionDto> sections = contractor.getSections().stream().map(dtoService::convertToDto).collect(Collectors.toList());
        String contractorImage = contractor.getImage();
        modelAndView.addObject("contractorImagePath",
                                contractorImage == null ? null :
                                ServletUriComponentsBuilder.fromCurrentContextPath()
                                    .path(contractorImageUrl)
                                    .path(contractorImage)
                                    .toUriString());
        modelAndView.addObject("sections", sections);
        modelAndView.addObject("defaultSectionName", SectionService.defaultSectionName());
        return modelAndView;
    }

    @GetMapping(value="/cabinet/new_section")
    public ModelAndView newSection() {
        ModelAndView modelAndView = new ModelAndView("contractor/new_section");
        modelAndView.addObject("sectionDto", new SectionDto());
        return modelAndView;
    }

    @GetMapping(value="/cabinet/modify_product/{productId}")
    public ModelAndView modifyProduct(@PathVariable long productId) {
        ModelAndView modelAndView = new ModelAndView("contractor/modify_product");
        Optional<Product> product = productRepository.findById(productId);
        if (!product.isPresent()) {
            throw new Error404Exception();
        }
        if (!product.get().getSection().getContractor().getName().equals(securityService.getCurrentUserName())) {
            throw new Error403Exception();
        }
        modelAndView.addObject("productDto", dtoService.convertToDto(product.get()));
        String productImage = product.get().getImage();
        modelAndView.addObject("productImagePath",
                productImage == null ? null :
                ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path(productImageUrl)
                        .path(productImage)
                        .toUriString());
        return modelAndView;
    }

    @PostMapping(value="/cabinet/modify_product/{productId}")
    public ModelAndView modifyProductPost(@PathVariable long productId, @ModelAttribute ProductDto productDto) {
        ModelAndView modelAndView = new ModelAndView("redirect:/cabinet");
        Product product = productRepository.findByProductId(productId);
        if ((product == null)) {
            modelAndView.addObject("errorMsg", "Failed to modify product");
            return modelAndView;
        }
        if (!product.getSection().getContractor().getName().equals(securityService.getCurrentUserName())) {
            throw new Error403Exception();
        }
        productService.modifyProduct(product, productDto);
        if (productRepository.save(product) == null) {
            modelAndView.addObject("errorMsg", "Failed to modify product");
            return modelAndView;
        }
        return modelAndView;
    }

    @GetMapping(value="/cabinet/modify_section/{sectionId}")
    public ModelAndView modifySection(@PathVariable long sectionId) {
        ModelAndView modelAndView = new ModelAndView("contractor/modify_section");
        Optional<Section> section = sectionRepository.findById(sectionId);
        if (!section.isPresent()) {
            throw new Error404Exception();
        }
        if (sectionService.isDefault(section.get())) {
            throw new Error404Exception();
        }
        if (!section.get().getContractor().getName().equals(securityService.getCurrentUserName())) {
            throw new Error403Exception();
        }
        modelAndView.addObject("sectionDto", dtoService.convertToDto(section.get()));
        return modelAndView;
    }

    @PostMapping(value="/cabinet/modify_section/{sectionId}")
    public ModelAndView modifySectionPost(@PathVariable long sectionId, @ModelAttribute SectionDto sectionDto) {
        ModelAndView modelAndView = new ModelAndView("redirect:/cabinet");
        Section section = sectionRepository.findBySectionId(sectionId);
        if ((section == null) || (sectionService.isDefault(section))) {
            modelAndView.addObject("errorMsg", "Failed to modify section");
            return modelAndView;
        }
        if (!section.getContractor().getName().equals(securityService.getCurrentUserName())) {
            throw new Error403Exception();
        }
        sectionService.modifySection(section, sectionDto);
        if (sectionRepository.save(section) == null) {
            modelAndView.addObject("errorMsg", "Failed to modify section");
            return modelAndView;
        }
        return modelAndView;
    }

    @PostMapping(value="/cabinet/add_section")
    public ModelAndView addSection(@ModelAttribute SectionDto sectionDto, BindingResult bindingResult) {
        String currentContractor = securityService.getCurrentUserName();
        if (currentContractor == null) {
            return new ModelAndView("redirect:/cabinet");
        }
        if (sectionRepository.findByName(sectionDto.getName()) != null) {
            ModelAndView errorModelAndView = new ModelAndView("redirect:/cabinet/new_section");
            errorModelAndView.addObject("errorMsg", "Section with name " + sectionDto.getName() + " already exists");
            return errorModelAndView;
        }
        Section newSection = dtoService.convertToSection(sectionDto);
        newSection.setContractor(contractorRepository.findByName(currentContractor));
        Section registeredSection = sectionService.registerNewSection(newSection);
        if (registeredSection == null) {
            ModelAndView errorModelAndView = new ModelAndView("redirect:/cabinet/new_section");
            errorModelAndView.addObject("errorMsg", "Error occured while creating section with name " + sectionDto.getName());
            return errorModelAndView;
        }
        ModelAndView modelAndView = new ModelAndView("redirect:/cabinet");
        return modelAndView;
    }

    @ResponseBody
    @PostMapping(value="/cabinet/remove_section/{sectionId}")
    public ResponseEntity<?> removeSection(@PathVariable Integer sectionId) {
        Section section = sectionRepository.findBySectionId(sectionId);
        Contractor sectionContractor = section.getContractor();
        Section defaultSection = contractorService.getDefaultSection(sectionContractor.getContractorId());
        if (!(sectionContractor.getName().equals(securityService.getCurrentUserName()))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (sectionService.isDefault(section)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (!sectionService.removeSection(section, defaultSection)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(sectionId, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping(value="/cabinet/remove_product/{productId}")
    public ResponseEntity<?> removeProduct(@PathVariable Integer productId) {
        Product product = productRepository.findByProductId(productId);
        Contractor productContractor = product.getSection().getContractor();
        if (!(productContractor.getName().equals(securityService.getCurrentUserName()))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        productRepository.delete(product);
        return new ResponseEntity<>(productId, HttpStatus.OK);
    }

    @GetMapping(value="/cabinet/new_product")
    public ModelAndView newProduct() {
        ModelAndView modelAndView = new ModelAndView("contractor/new_product");
        modelAndView.addObject("productDto", new ProductDto());
        return modelAndView;
    }

    @PostMapping(value="/cabinet/add_product")
    public ModelAndView addProduct(@ModelAttribute ProductDto productDto, BindingResult bindingResult) {
        String currentContractor = securityService.getCurrentUserName();
        if (currentContractor == null) {
            return new ModelAndView("redirect:/cabinet");
        }
        if (productRepository.findByName(productDto.getName()) != null) {
            ModelAndView errorModelAndView = new ModelAndView("redirect:/cabinet/new_product");
            errorModelAndView.addObject("errorMsg", "Product with name " + productDto.getName() + " already exists");
            return errorModelAndView;
        }
        Product newProduct = dtoService.convertToProduct(productDto);
        Product registeredProduct = productService.registerNewProduct(newProduct);
        if (registeredProduct == null) {
            ModelAndView errorModelAndView = new ModelAndView("redirect:/cabinet/new_product");
            errorModelAndView.addObject("errorMsg", "Error occured while creating product with name " + productDto.getName());
            return errorModelAndView;
        }
        ModelAndView modelAndView = new ModelAndView("redirect:/cabinet");
        return modelAndView;
    }

    @PostMapping(value = { "/cabinet/upload_image/main" })
    @ResponseBody
    public ResponseEntity<?> uploadMainImage(@RequestParam("file") @ValidImage MultipartFile image) {
        String name;
        Contractor contractor = contractorRepository.findByName(securityService.getCurrentUserName());
        String filename = "" + contractor.getContractorId();
        try {
            name = storageService.store(image, filename, IStorageService.FileType.MAIN_IMAGE);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new FileUploadResponse(), HttpStatus.NO_CONTENT);
        }
        String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(contractorImageUrl)
                .path(filename)
                .toUriString();
        contractor.setImage(Long.toString(contractor.getContractorId()));
        contractorRepository.save(contractor);

        return new ResponseEntity<>(new FileUploadResponse(name, uri, image.getContentType(), image.getSize()), HttpStatus.OK);
    }

    @PostMapping(value = { "/cabinet/upload_image/product/{productId}" })
    @ResponseBody
    public ResponseEntity<?> uploadProductImage(@RequestParam("file") @ValidImage MultipartFile image, @PathVariable long productId) {
        String name;
        Product product = productRepository.findByProductId(productId);
        String filename = "" + product.getProductId();
        try {
            name = storageService.store(image, filename, IStorageService.FileType.PRODUCT_IMAGE);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new FileUploadResponse(), HttpStatus.NO_CONTENT);
        }
        String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(productImageUrl)
                .path(filename)
                .toUriString();
        product.setImage(Long.toString(product.getProductId()));
        productRepository.save(product);

        return new ResponseEntity<>(new FileUploadResponse(name, uri, image.getContentType(), image.getSize()), HttpStatus.OK);
    }

}
