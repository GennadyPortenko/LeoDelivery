package com.cmdelivery.controller;

import com.cmdelivery.model.Contractor;
import com.cmdelivery.model.Person;
import com.cmdelivery.repository.ContractorRepository;
import com.cmdelivery.repository.PersonRepository;
import com.cmdelivery.repository.RoleRepository;
import com.cmdelivery.service.ContractorService;
import com.cmdelivery.service.PersonService;
import com.cmdelivery.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
    public ModelAndView home() {
        return new ModelAndView("chooseCity");
    }

    @GetMapping(value="/food/login")
    public ModelAndView regularLogin() {
        return new ModelAndView("regular/login");
    }

    @GetMapping(value="/contractor/login")
    public ModelAndView contractorLogin() {
        return new ModelAndView("contractor/login");
    }

    @GetMapping(value="/food")
    public ModelAndView regularHome() {
        return new ModelAndView("regular/food");
    }

    @GetMapping(value="/contractor/cabinet")
    public ModelAndView contractorHome() {
        return new ModelAndView("contractor/cabinet");
    }

}
