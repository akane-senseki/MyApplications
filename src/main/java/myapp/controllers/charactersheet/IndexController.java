package myapp.controllers.charactersheet;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {

    @Autowired
    HttpSession session; // セッション

    @RequestMapping(value = "/cs/index", method = RequestMethod.GET)
    public ModelAndView index(ModelAndView mv) {

        System.out.println("csindexController通過");

            mv.setViewName("views/charactersheet/index");

            return mv;

    }
}
