package com.bslota.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

   @GetMapping(value="/regular/hello")
   public String hello() {
          return "regular/hello";
   }

    @GetMapping(value="/")
    public String home() {
            return "regular/home";
    }

    @GetMapping(value="/regular/login")
    public String regularLogin() {
            return "regular/login";
    }

    @GetMapping(value="/contractor/login")
    public String contractorLogin() {
            return "contractor/login";
    }

    @GetMapping(value="/regular/home")
    public String regularHome() {
            return "regular/home";
    }

    @GetMapping(value="/contractor/home")
    public String contractorHome() {
            return "contractor/home";
    }

}
