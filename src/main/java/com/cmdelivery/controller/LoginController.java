package com.cmdelivery.controller;

import com.cmdelivery.config.component.ClientAuthenticationProvider;
import com.cmdelivery.dto.LoginStatus;
import com.cmdelivery.dto.OTPResponse;
import com.cmdelivery.model.*;
import com.cmdelivery.repository.*;
import com.cmdelivery.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class LoginController {
    private final PartnerService partnerService;
    private final PartnerRepository partnerRepository;

    private final ClientService clientService;
    private final ClientRepository clientRepository;
    private final SecurityService securityService;
    private final RoleRepository roleRepository;
    private final OTPService otpService;
    private final SmsService smsService;
    private final ClientAuthenticationProvider clientAuthenticationProvider;
    private final SectionService sectionService;
    private final SectionRepository sectionRepository;
    private final ProductRepository productRepository;
    private final CityRepository cityRepository;
    private final CategoryRepository categoryRepository;

    private final MessageSource messageSource;

    @Transactional
    @PostConstruct
    public void init() {
        final String PARTNER_USERNAME = "rest";
        final String PARTNER_EMAIL= "r@r.com";
        final String PARTNER_PASSWORD= "rest";
        final String PARTNER2_USERNAME = "rest2";
        final String PARTNER2_EMAIL= "r2@r2.com";
        final String PARTNER2_PASSWORD= "rest2";

        if (partnerRepository.findByName(PARTNER_USERNAME) == null) {
            Partner partner = new Partner(PARTNER_EMAIL, PARTNER_USERNAME, 30, 60, 50, PARTNER_PASSWORD, categoryRepository.findByCategoryId(3));
            partnerService.registerNewPartner(partner);
            Section defaultSection = sectionRepository.findByNameAndPartner(SectionService.defaultSectionName(), partner.getPartnerId());
            Set<Product> defaultProducts = new HashSet<>();

            Product product1 = new Product("Cheese cake", "Wonderful tasty cheese cake.");
            product1.setSection(defaultSection);
            productRepository.save(product1);

            Product product2 = new Product("Sushi", "Perfect sushi.");
            product2.setSection(defaultSection);

            productRepository.save(product2);

            Section pizza = new Section("Pizza");
            pizza.setDescription("Different pizzas");
            pizza.setPartner(partner);
            sectionRepository.save(pizza);

            Product product3 = new Product("Pizza", "Wonderful tasty pizza.");
            product3.setSection(pizza);
            productRepository.save(product3);

        }

        if (partnerRepository.findByName(PARTNER2_USERNAME) == null) {
            Partner partner = new Partner(PARTNER2_EMAIL, PARTNER2_USERNAME, 30, 60, 50, PARTNER2_PASSWORD, categoryRepository.findByCategoryId(3));
            partnerService.registerNewPartner(partner);
        }

    }

    @GetMapping(value="/")
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView("chooseLocation");
        modelAndView.addObject("cities", cityRepository.findAll());
        return modelAndView;
    }

    @GetMapping(value="/food/login")
    public ModelAndView clientLogin() {
        return new ModelAndView("client/login");
    }

    @GetMapping(value="/cabinet/login")
    public ModelAndView partnerLogin() {
        return new ModelAndView("partner/login");
    }


    @ResponseBody
    @PostMapping(value="/login/otp/{phone}")
    public ResponseEntity<?> otpRequest(@PathVariable String phone) {
        smsService.sendOTP(otpService.generateOTP(DtoService.parsePhone(phone)));
        return new ResponseEntity<>(new OTPResponse(true), HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping(value="/login/otp/ajax")
    public ResponseEntity<?> loginAJAX(@RequestBody LoginStatus data, HttpServletRequest request) {
        ResponseEntity<?> failedResponse = new ResponseEntity<>(new LoginStatus(data.getPhone(), data.getOtp(), false), HttpStatus.OK);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(data.getPhone(), data.getOtp());
        Authentication authentication = clientAuthenticationProvider.authenticate(token);
        if (authentication != null) {
            Client client = clientRepository.findByPhone(authentication.getName());
            if (client == null) {
                if (clientService.registerNewClient(new Client(DtoService.parsePhone(data.getPhone()))) == null) {
                    return failedResponse;
                }
            }
            SecurityContextHolder.getContext().setAuthentication(authentication);
            HttpSession session = request.getSession();
            session.setAttribute("username", DtoService.toMaskedPhone(authentication.getName()));
            session.setAttribute("role", "CLIENT");
            return new ResponseEntity<>(new LoginStatus(data.getPhone(), data.getOtp(), true), HttpStatus.OK);
        } else {
            return failedResponse;
        }
    }

}
