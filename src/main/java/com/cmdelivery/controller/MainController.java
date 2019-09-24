package com.cmdelivery.controller;

import com.cmdelivery.exception.Error404Exception;
import com.cmdelivery.model.Category;
import com.cmdelivery.model.Partner;
import com.cmdelivery.repository.CategoryRepository;
import com.cmdelivery.repository.PartnerRepository;
import com.cmdelivery.service.CategoryService;
import com.cmdelivery.service.DtoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class MainController {
    private final PartnerRepository partnerRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;
    private final DtoService dtoService;

    @RequestMapping(value="/food", method={ RequestMethod.GET, RequestMethod.POST })
    public ModelAndView food(@RequestParam(required=false) List<String> categories) {
        ModelAndView modelAndView = new ModelAndView("client/food");
        modelAndView.addObject("categories", categoryRepository.findAll().stream().map(dtoService::convertToDto).collect(Collectors.toList()));

        if (categories != null) {
            List<Category> selectedCategories = categoryService.findByNameIn(categories);
            modelAndView.addObject("selectedCategories", selectedCategories.stream().map(dtoService::convertToDto).collect(Collectors.toList()));
            modelAndView.addObject("restaurants",
                    partnerRepository.findByMainCategoryIn(selectedCategories).stream().map(dtoService::convertToDto).collect(Collectors.toList()));
        } else {
            modelAndView.addObject("restaurants",
                    partnerRepository.findAll().stream().map(dtoService::convertToDto).collect(Collectors.toList()));
        }

        return modelAndView;
    }

    @GetMapping(value="/food/restaurant/{restaurantName}")
    public ModelAndView restaurant(@PathVariable String restaurantName) {
        Partner restaurant = partnerRepository.findByName(restaurantName);
        if (restaurant == null) {
            throw new Error404Exception();
        }
        ModelAndView modelAndView = new ModelAndView("client/restaurant");
        modelAndView.addObject("restaurant", dtoService.convertToDto(restaurant));
        return modelAndView;
    }

}
