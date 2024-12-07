package khuong.com.lasttermjava.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping
    public String home() {
        return "home";
    }

    @GetMapping
    public String account() {
        return "account";
    }

    @GetMapping
    public String login() {
        return "login";
    }


}

