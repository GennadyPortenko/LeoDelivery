package com.cmdelivery.controller;

import com.cmdelivery.dto.ProductDto;
import com.cmdelivery.dto.SectionDto;
import com.cmdelivery.model.Contractor;
import com.cmdelivery.model.Product;
import com.cmdelivery.model.Section;
import com.cmdelivery.repository.ContractorRepository;
import com.cmdelivery.repository.ProductRepository;
import com.cmdelivery.repository.SectionRepository;
import com.cmdelivery.service.DtoService;
import com.cmdelivery.service.ProductService;
import com.cmdelivery.service.SectionService;
import com.cmdelivery.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class CabinetController {
    private final ContractorRepository contractorRepository;
    private final SecurityService securityService;
    private final DtoService dtoService;
    private final SectionService sectionService;
    private final SectionRepository sectionRepository;
    private final ProductService productService;
    private final ProductRepository productRepository;

    @GetMapping(value="/contractor/cabinet")
    public ModelAndView contractorHome(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("contractor/cabinet");
        Contractor contractor = contractorRepository.findByName(securityService.getCurrentUserName());
        if (contractor == null) {
            return modelAndView;
        }
        List<SectionDto> sections = contractor.getSections().stream().map(dtoService::convertToDto).collect(Collectors.toList());
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

    @Transactional
    @ResponseBody
    @PostMapping(value="/contractor/cabinet/remove_section/{sectionId}")
    public ResponseEntity<?> removeSection(@PathVariable Integer sectionId) {
        System.out.println(sectionRepository.findBySectionId(sectionId).getContractor().getName());
        System.out.println(securityService.getCurrentUserName());
        if (!(sectionRepository.findBySectionId(sectionId).getContractor().getName().equals(securityService.getCurrentUserName()))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (sectionService.deleteById(sectionId) == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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

}
