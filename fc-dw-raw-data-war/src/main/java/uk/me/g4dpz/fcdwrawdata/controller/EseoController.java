package uk.me.g4dpz.fcdwrawdata.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class EseoController {

    @GetMapping("/ui/eseo")
    public ModelAndView display() {
        ModelAndView modelAndView = new ModelAndView("content/eseo/index");
        return modelAndView;
    }

}
