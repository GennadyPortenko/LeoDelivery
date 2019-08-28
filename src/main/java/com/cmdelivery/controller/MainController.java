package com.cmdelivery.controller;

import com.cmdelivery.dto.SectionDto;
import com.cmdelivery.model.Contractor;
import com.cmdelivery.repository.CategoryRepository;
import com.cmdelivery.repository.ContractorRepository;
import com.cmdelivery.service.SectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class MainController {
    private final ContractorRepository contractorRepository;
    private final CategoryRepository categoryRepository;

    @GetMapping(value="/food")
    public ModelAndView regularHome() {
        ModelAndView modelAndView = new ModelAndView("regular/food");
        modelAndView.addObject("restaurants", contractorRepository.findAll());
        modelAndView.addObject("categories", categoryRepository.findAll());
        return modelAndView;
    }

}
