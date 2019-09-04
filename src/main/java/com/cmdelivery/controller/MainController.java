package com.cmdelivery.controller;

import com.cmdelivery.exception.Error404Exception;
import com.cmdelivery.model.Partner;
import com.cmdelivery.repository.CategoryRepository;
import com.cmdelivery.repository.PartnerRepository;
import com.cmdelivery.service.DtoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class MainController {
    private final PartnerRepository partnerRepository;
    private final CategoryRepository categoryRepository;
    private final DtoService dtoService;

    @RequestMapping(value="/food", method={ RequestMethod.GET, RequestMethod.POST })
    public ModelAndView food() {
        ModelAndView modelAndView = new ModelAndView("client/food");
        modelAndView.addObject("restaurants",
                partnerRepository.findAll().stream().map(dtoService::convertToDto).collect(Collectors.toList()));
        modelAndView.addObject("categories", categoryRepository.findAll());
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
