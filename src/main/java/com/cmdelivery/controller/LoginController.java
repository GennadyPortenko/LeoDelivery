package com.cmdelivery.controller;

import com.cmdelivery.dto.OTPResponse;
import com.cmdelivery.model.Contractor;
import com.cmdelivery.model.Person;
import com.cmdelivery.repository.ContractorRepository;
import com.cmdelivery.repository.PersonRepository;
import com.cmdelivery.repository.RoleRepository;
import com.cmdelivery.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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
    private final OTPService otpService;
    private final SmsService smsService;

    @PostConstruct
    public void init() {
        final String CONTRACTOR_USERNAME = "rest";
        final String CONTRACTOR_EMAIL= "rest@rest.com";
        final String CONTRACTOR_PASSWORD= "rest";
        final String PERSON_PHONE= "1111111111";
        final String PERSON_PASSWORD= "user";
        if (contractorRepository.findByName(CONTRACTOR_USERNAME) == null) {
            contractorService.registerNewContractor(new Contractor(CONTRACTOR_EMAIL, CONTRACTOR_USERNAME, CONTRACTOR_PASSWORD));
        }
        if (personRepository.findByPhone(PERSON_PHONE) == null) {
            personService.registerNewPerson(new Person(PERSON_PHONE, PERSON_PASSWORD));
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

    @ResponseBody
    @PostMapping(value="/login/otp/{phone}")
    public ResponseEntity<?> otpRequest(@PathVariable String phone) {
        smsService.sendOTP(otpService.generateOTP(DtoService.parsePhone(phone)));
        return new ResponseEntity<>(new OTPResponse(true), HttpStatus.OK);
    }

}
