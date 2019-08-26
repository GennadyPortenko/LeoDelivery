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
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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

    @Value("${contractor.image.prefix}")
    private String contractorImagePrefix;

    @GetMapping(value="/contractor/cabinet")
    public ModelAndView contractorHome(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("contractor/cabinet");
        String username = securityService.getCurrentUserName();
        Contractor contractor = contractorRepository.findByName(username);
        if (contractor == null) {
            return modelAndView;
        }
        List<SectionDto> sections = contractor.getSections().stream().map(dtoService::convertToDto).collect(Collectors.toList());
        modelAndView.addObject("imagePath", contractor.getImage());
        modelAndView.addObject("sections", sections);
        modelAndView.addObject("defaultSectionName", SectionService.defaultSectionName());
        return modelAndView;
    }

    @GetMapping(value="/contractor/cabinet/new_section")
    public ModelAndView newSection() {
        ModelAndView modelAndView = new ModelAndView("contractor/new_section");
        modelAndView.addObject("sectionDto", new SectionDto());
        return modelAndView;
    }

    @GetMapping(value="/contractor/cabinet/modify_section/{sectionId}")
    public ModelAndView modifySection(@PathVariable long sectionId) {
        ModelAndView modelAndView = new ModelAndView("contractor/modify_section");
        Optional<Section> section = sectionRepository.findById(sectionId);
        if (!section.isPresent()) {
            System.out.println("default");
            throw new Error404Exception();
        }
        if (sectionService.isDefault(section.get())) {
            System.out.println("default");
            throw new Error404Exception();
        }
        if (!section.get().getContractor().getName().equals(securityService.getCurrentUserName())) {
            throw new Error403Exception();
        }
        modelAndView.addObject("sectionDto", dtoService.convertToDto(section.get()));
        return modelAndView;
    }

    @PostMapping(value="/contractor/cabinet/modify_section/{sectionId}")
    public ModelAndView modifySectionPost(@PathVariable long sectionId, @ModelAttribute SectionDto sectionDto) {
        ModelAndView modelAndView = new ModelAndView("redirect:/contractor/cabinet");
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

    @PostMapping(value="/contractor/cabinet/add_section")
    public ModelAndView addSection(@ModelAttribute SectionDto sectionDto, BindingResult bindingResult) {
        String currentContractor = securityService.getCurrentUserName();
        if (currentContractor == null) {
            return new ModelAndView("redirect:/contractor/cabinet");
        }
        if (sectionRepository.findByName(sectionDto.getName()) != null) {
            ModelAndView errorModelAndView = new ModelAndView("redirect:/contractor/cabinet/new_section");
            errorModelAndView.addObject("errorMsg", "Section with name " + sectionDto.getName() + " already exists");
            return errorModelAndView;
        }
        Section newSection = dtoService.convertToSection(sectionDto);
        newSection.setContractor(contractorRepository.findByName(currentContractor));
        Section registeredSection = sectionService.registerNewSection(newSection);
        if (registeredSection == null) {
            ModelAndView errorModelAndView = new ModelAndView("redirect:/contractor/cabinet/new_section");
            errorModelAndView.addObject("errorMsg", "Error occured while creating section with name " + sectionDto.getName());
            return errorModelAndView;
        }
        ModelAndView modelAndView = new ModelAndView("redirect:/contractor/cabinet");
        return modelAndView;
    }

    @ResponseBody
    @PostMapping(value="/contractor/cabinet/remove_section/{sectionId}")
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

    @GetMapping(value="/contractor/cabinet/new_product")
    public ModelAndView newProduct() {
        ModelAndView modelAndView = new ModelAndView("contractor/new_product");
        modelAndView.addObject("productDto", new ProductDto());
        return modelAndView;
    }

    @PostMapping(value="/contractor/cabinet/add_product")
    public ModelAndView addProduct(@ModelAttribute ProductDto productDto, BindingResult bindingResult) {
        String currentContractor = securityService.getCurrentUserName();
        if (currentContractor == null) {
            return new ModelAndView("redirect:/contractor/cabinet");
        }
        if (productRepository.findByName(productDto.getName()) != null) {
            ModelAndView errorModelAndView = new ModelAndView("redirect:/contractor/cabinet/new_product");
            errorModelAndView.addObject("errorMsg", "Product with name " + productDto.getName() + " already exists");
            return errorModelAndView;
        }
        Product newProduct = dtoService.convertToProduct(productDto);
        Product registeredProduct = productService.registerNewProduct(newProduct);
        if (registeredProduct == null) {
            ModelAndView errorModelAndView = new ModelAndView("redirect:/contractor/cabinet/new_product");
            errorModelAndView.addObject("errorMsg", "Error occured while creating product with name " + productDto.getName());
            return errorModelAndView;
        }
        ModelAndView modelAndView = new ModelAndView("redirect:/contractor/cabinet");
        return modelAndView;
    }

    @PostMapping(value = { "/contractor/cabinet/upload_image" })
    @ResponseBody
    public ResponseEntity<?> uploadImage(@RequestParam("file") @ValidImage MultipartFile image) {
        String name;
        Contractor contractor = contractorRepository.findByName(securityService.getCurrentUserName());
        String filename = contractorImagePrefix + contractor.getContractorId();
        try {
            name = storageService.store(image, filename);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new FileUploadResponse(), HttpStatus.NO_CONTENT);
        }
        String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(contractorImageUrl)
                .path(filename)
                .toUriString();
        contractor.setImage(uri);
        contractorRepository.save(contractor);

        return new ResponseEntity<>(new FileUploadResponse(name, uri, image.getContentType(), image.getSize()), HttpStatus.OK);
    }

}
