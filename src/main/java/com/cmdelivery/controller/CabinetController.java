package com.cmdelivery.controller;

import com.cmdelivery.dto.*;
import com.cmdelivery.exception.Error403Exception;
import com.cmdelivery.exception.Error404Exception;
import com.cmdelivery.model.Partner;
import com.cmdelivery.model.Product;
import com.cmdelivery.model.Section;
import com.cmdelivery.repository.CategoryRepository;
import com.cmdelivery.repository.PartnerRepository;
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
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class CabinetController {
    private final PartnerRepository partnerRepository;
    private final PartnerService partnerService;
    private final SecurityService securityService;
    private final DtoService dtoService;
    private final SectionService sectionService;
    private final SectionRepository sectionRepository;
    private final ProductService productService;
    private final ProductRepository productRepository;
    private final IStorageService storageService;
    private final CategoryRepository categoryRepository;

    @Value("${partner.image.url}")
    private String partnerImageUrl;
    @Value("${partner.logo.url}")
    private String partnerLogoUrl;
    @Value("${product.image.url}")
    private String productImageUrl;

    private String partnerSettingsBindingResultString = "org.springframework.validation.BindingResult.partnerSettingsDto";

    @GetMapping(value="/cabinet")
    public ModelAndView partnerHome(ModelMap modelMap) {
        ModelAndView modelAndView = new ModelAndView("partner/cabinet");

        // get PartnerSettingsDto object after redirect to this url from /cabinet/modify_partner_settings to display Bindingresult errors
        PartnerSettingsDto redirectPartnerSettingsDto = (PartnerSettingsDto) modelMap.get("partnerSettingsDto");
        BindingResult bindingResult = (BindingResult) modelMap.get(partnerSettingsBindingResultString);
        if (bindingResult != null) {
            if (bindingResult.hasErrors()) {
                modelAndView.addObject("errorMessage", "Failed to update the settings. There are incorrect values.");
            }
        }

        Partner partner = partnerRepository.findByName(securityService.getCurrentUserName());
        if (partner == null) {
            return new ModelAndView("redirect:/cabinet/login");
        }
        modelAndView.addObject("partner", dtoService.convertToDto(partner));
        modelAndView.addObject("defaultSectionName", SectionService.defaultSectionName());
        modelAndView.addObject("partnerSettingsDto", redirectPartnerSettingsDto != null ? redirectPartnerSettingsDto
                                                                                                          : dtoService.getPartnerSettings(partner));
        modelAndView.addObject("categories", categoryRepository.findAll().stream().map(dtoService::convertToDto).peek(c -> System.out.println(c.getId())).collect(Collectors.toList()));
        return modelAndView;
    }

    @PostMapping(value="/cabinet/modify_partner_settings")
    public ModelAndView modifyPartnerSettings(@ModelAttribute @Valid PartnerSettingsDto partnerSettingsDto, BindingResult bindingResult,
                                                 RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView("redirect:/cabinet");
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(partnerSettingsBindingResultString, bindingResult);
            redirectAttributes.addFlashAttribute("partnerSettingsDto", partnerSettingsDto);
            return modelAndView;
        }
        Partner partner = partnerRepository.findByName(securityService.getCurrentUserName());
        if (partner == null) {
            return modelAndView;
        }
        partner.setMinTime(partnerSettingsDto.getMinTime());
        partner.setMaxTime(partnerSettingsDto.getMaxTime());
        partner.setMinPrice(partnerSettingsDto.getMinPrice());
        Partner savedPartner = partnerRepository.save(partner);
        if (savedPartner == null) {
            modelAndView.addObject("errorMessage", "Failed to update the settings");
        }

        return modelAndView;
    }

    @GetMapping(value="/cabinet/new_section")
    public ModelAndView newSection() {
        ModelAndView modelAndView = new ModelAndView("partner/new_section");
        modelAndView.addObject("sectionDto", new SectionDto());
        return modelAndView;
    }

    @GetMapping(value="/cabinet/modify_product/{productId}")
    public ModelAndView modifyProduct(@PathVariable long productId, ModelMap modelMap) {

        ModelAndView modelAndView = new ModelAndView("partner/modify_product");

        String errorMessage = (String) modelMap.get("errorMessage");
        if (errorMessage != null) {
            modelAndView.addObject("errorMessage", errorMessage);
        }

        Product product = productRepository.findByProductId(productId);
        if (product == null) {
            throw new Error404Exception();
        }
        if (!product.getSection().getPartner().getName().equals(securityService.getCurrentUserName())) {
            throw new Error403Exception();
        }
        modelAndView.addObject("product", dtoService.convertToDto(product));
        return modelAndView;
    }

    @PostMapping(value="/cabinet/modify_product/{productId}")
    public ModelAndView modifyProductPost(@PathVariable long productId, @ModelAttribute ProductDto productDto,
                                          BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView("redirect:/cabinet/modify_product/" + productId);
        if (productRepository.findByName(productDto.getName()) != null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Product with name " + productDto.getName() + " already exists.");
            return modelAndView;
        }
        Product product = productRepository.findByProductId(productId);
        if ((product == null)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to modify product. Product not found.");
            return modelAndView;
        }
        if (!product.getSection().getPartner().getName().equals(securityService.getCurrentUserName())) {
            throw new Error403Exception();
        }
        productService.modifyProduct(product, productDto);
        Product savedProduct = productRepository.save(product);
        if (savedProduct == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to save the product.");
            return modelAndView;
        }
        return modelAndView;
    }

    @GetMapping(value="/cabinet/modify_section/{sectionId}")
    public ModelAndView modifySection(@PathVariable long sectionId) {
        ModelAndView modelAndView = new ModelAndView("partner/modify_section");
        Optional<Section> section = sectionRepository.findById(sectionId);
        if (!section.isPresent()) {
            throw new Error404Exception();
        }
        if (sectionService.isDefault(section.get())) {
            throw new Error404Exception();
        }
        if (!section.get().getPartner().getName().equals(securityService.getCurrentUserName())) {
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
            modelAndView.addObject("errorMessage", "Failed to modify section");
            return modelAndView;
        }
        if (!section.getPartner().getName().equals(securityService.getCurrentUserName())) {
            throw new Error403Exception();
        }
        sectionService.modifySection(section, sectionDto);
        if (sectionRepository.save(section) == null) {
            modelAndView.addObject("errorMessage", "Failed to modify section");
            return modelAndView;
        }
        return modelAndView;
    }

    @PostMapping(value="/cabinet/new_section")
    public ModelAndView addSection(@ModelAttribute SectionDto sectionDto, BindingResult bindingResult) {
        String currentPartner = securityService.getCurrentUserName();
        if (currentPartner == null) {
            return new ModelAndView("redirect:/cabinet");
        }
        if (sectionRepository.findByName(sectionDto.getName()) != null) {
            ModelAndView errorModelAndView = new ModelAndView("redirect:/cabinet/new_section");
            errorModelAndView.addObject("errorMessage", "Section with name " + sectionDto.getName() + " already exists");
            return errorModelAndView;
        }
        Section newSection = dtoService.convertToSection(sectionDto);
        newSection.setPartner(partnerRepository.findByName(currentPartner));
        Section registeredSection = sectionService.registerNewSection(newSection);
        if (registeredSection == null) {
            ModelAndView errorModelAndView = new ModelAndView("redirect:/cabinet/new_section");
            errorModelAndView.addObject("errorMessage", "Error occured while creating section with name " + sectionDto.getName());
            return errorModelAndView;
        }
        ModelAndView modelAndView = new ModelAndView("redirect:/cabinet");
        return modelAndView;
    }

    @ResponseBody
    @PostMapping(value="/cabinet/remove_section/{sectionId}")
    public ResponseEntity<?> removeSection(@PathVariable long sectionId) {
        Section section = sectionRepository.findBySectionId(sectionId);
        Partner sectionPartner = section.getPartner();
        Section defaultSection = partnerService.getDefaultSection(sectionPartner.getPartnerId());
        if (!(sectionPartner.getName().equals(securityService.getCurrentUserName()))) {
            return new ResponseEntity<>(new AjaxResponse(false, "Access denied"), HttpStatus.OK);
        }
        if (sectionService.isDefault(section)) {
            return new ResponseEntity<>(new AjaxResponse(false, "You can't remove default section"), HttpStatus.OK);
        }
        if (!sectionService.removeSection(section, defaultSection)) {
            return new ResponseEntity<>(new AjaxResponse(false, "Failed to remove section"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new AjaxResponse(true, ""), HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping(value="/cabinet/remove_product/{productId}")
    public ResponseEntity<?> removeProduct(@PathVariable long productId) {
        Product product = productRepository.findByProductId(productId);
        Partner productPartner = product.getSection().getPartner();
        if (!(productPartner.getName().equals(securityService.getCurrentUserName()))) {
            return new ResponseEntity<>(new AjaxResponse(false, "Access denied"), HttpStatus.OK);
        }
        productRepository.delete(product);
        return new ResponseEntity<>(new AjaxResponse(true, ""), HttpStatus.OK);
    }

    @GetMapping(value="/cabinet/new_product")
    public ModelAndView newProduct() {
        ModelAndView modelAndView = new ModelAndView("partner/new_product");
        modelAndView.addObject("productDto", new ProductDto());
        return modelAndView;
    }

    @PostMapping(value="/cabinet/new_product")
    public ModelAndView addProduct(@ModelAttribute ProductDto productDto, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView("redirect:/cabinet");
        ModelAndView errorModelAndView = new ModelAndView("partner/new_product");
        String currentPartner = securityService.getCurrentUserName();
        if (currentPartner == null) {
            return modelAndView;
        }
        if (productRepository.findByName(productDto.getName()) != null) {
            errorModelAndView.addObject("errorMessage", "Product with name " + productDto.getName() + " already exists");
            return errorModelAndView;
        }
        Product newProduct = dtoService.convertToProduct(productDto);
        Product registeredProduct = productService.registerNewProduct(newProduct);
        if (registeredProduct == null) {
            errorModelAndView.addObject("errorMessage", "Error occured while creating product with name " + productDto.getName());
            return errorModelAndView;
        }
        return modelAndView;
    }

    @PostMapping(value = { "/cabinet/upload_image/main" })
    @ResponseBody
    public ResponseEntity<?> uploadMainImage(@RequestParam("file") @ValidImage MultipartFile image) {
        String name;
        Partner partner = partnerRepository.findByName(securityService.getCurrentUserName());
        String filename = "" + partner.getPartnerId();
        try {
            name = storageService.store(image, filename, IStorageService.FileType.MAIN_IMAGE);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new FileUploadResponse(), HttpStatus.NO_CONTENT);
        }
        String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(partnerImageUrl)
                .path(filename)
                .toUriString();
        partner.setImage(Long.toString(partner.getPartnerId()));
        partnerRepository.save(partner);

        return new ResponseEntity<>(new FileUploadResponse(name, uri, image.getContentType(), image.getSize()), HttpStatus.OK);
    }

    @PostMapping(value = { "/cabinet/upload_image/logo" })
    @ResponseBody
    public ResponseEntity<?> uploadLogoImage(@RequestParam("file") @ValidImage MultipartFile logo) {
        String name;
        Partner partner = partnerRepository.findByName(securityService.getCurrentUserName());
        String filename = "" + partner.getPartnerId();
        try {
            name = storageService.store(logo, filename, IStorageService.FileType.LOGO_IMAGE);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new FileUploadResponse(), HttpStatus.NO_CONTENT);
        }
        String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(partnerLogoUrl)
                .path(filename)
                .toUriString();
        partner.setLogo(Long.toString(partner.getPartnerId()));
        partnerRepository.save(partner);

        return new ResponseEntity<>(new FileUploadResponse(name, uri, logo.getContentType(), logo.getSize()), HttpStatus.OK);
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

    @ResponseBody
    @PostMapping(value="/cabinet/move_product_to_section/{productId}/{sectionId}")
    public ResponseEntity<?> moveProductToSection(@PathVariable long productId, @PathVariable int sectionId) {
        Product product = productRepository.findByProductId(productId);
        if (product.getSection().getSectionId() == sectionId) {
            return new ResponseEntity<>(new AjaxResponse(false, "The product is already in target section"), HttpStatus.OK);
        }

        Partner productPartner = product.getSection().getPartner();
        if (!(productPartner.getName().equals(securityService.getCurrentUserName()))) {
            return new ResponseEntity<>(new AjaxResponse(false, "Access denied"), HttpStatus.OK);
        }

        Product movedProduct = productService.moveToSection(product, sectionRepository.findBySectionId(sectionId));
        if (movedProduct == null) {
            return new ResponseEntity<>(new AjaxResponse(false, "Failed to move project to target section"), HttpStatus.OK);
        }

        return new ResponseEntity<>(new AjaxResponse(true, ""), HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping(value="/cabinet/set_main_category/{categoryId}")
    public ResponseEntity<?> setMainCategory(@PathVariable int categoryId) {
        String errorMessage = "Failed to set main category";
        Partner partner = partnerRepository.findByName(securityService.getCurrentUserName());
        if (partner == null) {
            return new ResponseEntity<>(new AjaxResponse(false, "Access denied"), HttpStatus.OK);
        }
        if (partner.getMainCategory().getCategoryId() == categoryId) {
            return new ResponseEntity<>(new AjaxResponse(false, "Target category is already set as a main category"), HttpStatus.OK);
        }
        if (partnerService.setMainCategory(partner, categoryId) == null) {
            return new ResponseEntity<>(new AjaxResponse(false, errorMessage), HttpStatus.OK);
        }
        return new ResponseEntity<>(new AjaxResponse(true, ""), HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping(value="/cabinet/add_category/{categoryId}")
    public ResponseEntity<?> addPartnerCategory(@PathVariable int categoryId) {
        String errorMessage = "Failed to add the category";
        Partner partner = partnerRepository.findByName(securityService.getCurrentUserName());
        if (partner == null) {
            return new ResponseEntity<>(new AjaxResponse(false, "Access denied"), HttpStatus.OK);
        }
        if (partner.getCategories().contains(categoryRepository.findByCategoryId(categoryId))) {
            return new ResponseEntity<>(new AjaxResponse(false, "The category has been added already"), HttpStatus.OK);
        }
        if (partnerService.addCategory(partner, categoryId) == null) {
            return new ResponseEntity<>(new AjaxResponse(false, errorMessage), HttpStatus.OK);
        }
        return new ResponseEntity<>(new AjaxResponse(true, ""), HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping(value="/cabinet/remove_category/{categoryId}")
    public ResponseEntity<?> removePartnerCategory(@PathVariable int categoryId) {
        String errorMessage = "Failed to remove the category";
        Partner partner = partnerRepository.findByName(securityService.getCurrentUserName());
        if (partner == null) {
            return new ResponseEntity<>(new AjaxResponse(false, "Access denied"), HttpStatus.OK);
        }
        if (partnerService.removeCategory(partner, categoryId) == null) {
            return new ResponseEntity<>(new AjaxResponse(false, errorMessage), HttpStatus.OK);
        }
        return new ResponseEntity<>(new AjaxResponse(true, ""), HttpStatus.OK);
    }

}
