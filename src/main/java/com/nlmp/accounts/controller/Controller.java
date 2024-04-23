package com.nlmp.accounts.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class Controller {

    @GetMapping("/hello")
    public String hello(){
        return "Helloo one";
    }


}
