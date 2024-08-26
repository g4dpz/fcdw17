package uk.me.g4dpz.fcdwrawdata.controller;

import uk.me.g4dpz.fcdwrawdata.service.FitterMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FuncubeOneController {

    private final FitterMessageService fitterMessageService;

    @Autowired
    public FuncubeOneController(FitterMessageService fitterMessageService) {
        this.fitterMessageService = fitterMessageService;
    }

    @GetMapping("/ui/fc1-fm")
    public ModelAndView display() {
        ModelAndView modelAndView = new ModelAndView("content/fc1-fm/index");
        return modelAndView;
    }

    @GetMapping("/ui/fc1-fm/fitter")
    public ModelAndView fitter() {
        ModelAndView modelAndView = new ModelAndView("content/fc1-fm/fitter");
        modelAndView.addObject("fitterMessages", fitterMessageService.getMessages("http://localhost:10080/funcube/data/fitter"));
        return modelAndView;
    }

    @GetMapping("/ui/fc1-fm/wod")
    public ModelAndView wod() {
        ModelAndView modelAndView = new ModelAndView("content/fc1-fm/wod");
        return modelAndView;
    }

}
