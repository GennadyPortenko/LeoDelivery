package com.bslota.controller;

import com.bslota.model.Contractor;
import com.bslota.model.Person;
import com.bslota.repository.ContractorRepository;
import com.bslota.repository.PersonRepository;
import com.bslota.repository.RoleRepository;
import com.bslota.service.ContractorService;
import com.bslota.service.PersonService;
import com.bslota.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;

@Controller
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class LoginController {
    private final ContractorService contractorService;
    private final ContractorRepository contractorRepository;

    private final PersonService personService;
    private final PersonRepository personRepository;
    private final SecurityService securityService;
    private final RoleRepository roleRepository;

    @PostConstruct
    public void init() {
        final String CONTRACTOR_USERNAME = "rest";
        final String CONTRACTOR_EMAIL= "rest@rest.com";
        final String CONTRACTOR_PASSWORD= "rest";
        final String PERSON_USERNAME = "user";
        final String PERSON_EMAIL = "user@user.com";
        final String PERSON_PASSWORD= "user";
        if (contractorRepository.findByName(CONTRACTOR_USERNAME) == null) {
            contractorService.registerNewContractor(new Contractor(CONTRACTOR_EMAIL, CONTRACTOR_USERNAME, CONTRACTOR_PASSWORD));
        }
        if (personRepository.findByName(PERSON_USERNAME) == null) {
            personService.registerNewPerson(new Person(PERSON_EMAIL, PERSON_USERNAME, PERSON_PASSWORD));
        }
    }

    @GetMapping(value="/")
    public String home() {
            return "regular/home";
    }

    @GetMapping(value="/regular/login")
    public ModelAndView regularLogin() {
        ModelAndView modelAndView =  new ModelAndView("regular/login");
        modelAndView.addObject("username", securityService.getCurrentUserName());
        return modelAndView;
    }

    @GetMapping(value="/contractor/login")
    public ModelAndView contractorLogin() {
        ModelAndView modelAndView = new ModelAndView("contractor/login");
        modelAndView.addObject("username", securityService.getCurrentUserName());
        return modelAndView;
    }

    @GetMapping(value="/regular/home")
    public ModelAndView regularHome() {
        ModelAndView modelAndView = new ModelAndView("regular/home");
        modelAndView.addObject("username", securityService.getCurrentUserName());
        return modelAndView;
    }

    @GetMapping(value="/contractor/cabinet")
    public ModelAndView contractorHome() {
        ModelAndView modelAndView = new ModelAndView("contractor/cabinet");
        modelAndView.addObject("username", securityService.getCurrentUserName());
        return modelAndView;
    }

}
